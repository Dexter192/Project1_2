package gameEngine3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public class Splines {

    public final float[] heightMap;
    public final short width;
    public final short height;
    public final float[] vertices;
    public final short[] indices;

    public final int vertexSize;
    private final int positionSize = 3;

    public Splines	(int width, int height, int vertexSize, String heightMapTexture) {

        if ((width + 1) * (height + 1) > Short.MAX_VALUE) {
            throw new IllegalArgumentException(            
                    "Chunk size too big, (width + 1)*(height+1) must be <= 32767");
        }

        this.heightMap = new float[(width + 1) * (height + 1)];
        this.width = (short) width;
        this.height = (short) height;
        this.vertices = new float[heightMap.length * vertexSize];
        this.indices = new short[width * height * 6];
        this.vertexSize = vertexSize;

        buildHeightmap(heightMapTexture);

        buildIndices();
        buildVertices();

        calcNormals(indices, vertices);

    }

    public void buildHeightmap(String pathToHeightMap) {

        FileHandle handle = Gdx.files.internal(pathToHeightMap);
        Pixmap heightmapImage = new Pixmap(handle);
        Color color = new Color();
        int idh = 0;

        for (int x = 0; x < this.width + 1; x++) {
            for (int y = 0; y < this.height + 1; y++) {
//            	Color.rgba8888((float)Math.random()*256, (float)Math.random()*256, (float)Math.random()*256, 1);
                Color.rgba8888ToColor(color, heightmapImage.getPixel(x, y));
//                this.heightMap[idh++] = color.r;
                this.heightMap[idh++] = (float) (Math.random());
            }
        }
    }

    public void buildVertices() {
        int heightPitch = height + 1;
        int widthPitch = width + 1;

        int idx = 0;
        int hIdx = 0;
        int strength = 10; // multiplier for height map

        float scale = 4f;

        for (int z = 0; z < heightPitch; z++) {
            for (int x = 0; x < widthPitch; x++) {

                // POSITION
                vertices[idx++] = scale * x;
                vertices[idx++] = heightMap[hIdx++] * strength;
                vertices[idx++] = scale * z;

                // NORMAL, skip these for now
                idx += 3;

                // COLOR
                vertices[idx++] = Color.WHITE.toFloatBits();

                // TEXTURE
                vertices[idx++] = (x / (float) width);
                vertices[idx++] = (z / (float) height);

            }
        }
    }

    private void buildIndices() {
        int idx = 0;
        short pitch = (short) (width + 1);
        short i1 = 0;
        short i2 = 1;
        short i3 = (short) (1 + pitch);
        short i4 = pitch;

        short row = 0;

        for (int z = 0; z < height; z++) {
            for (int x = 0; x < width; x++) {
                indices[idx++] = i1;
                indices[idx++] = i2;
                indices[idx++] = i3;

                indices[idx++] = i3;
                indices[idx++] = i4;
                indices[idx++] = i1;

                i1++;
                i2++;
                i3++;
                i4++;
            }

            row += pitch;
            i1 = row;
            i2 = (short) (row + 1);
            i3 = (short) (i2 + pitch);
            i4 = (short) (row + pitch);
        }
    }

    // Gets the index of the first float of a normal for a specific vertex
    private int getNormalStart(int vertIndex) {
        return vertIndex * vertexSize + positionSize;
    }

    // Gets the index of the first float of a specific vertex
    private int getPositionStart(int vertIndex) {
        return vertIndex * vertexSize;
    }

    // Adds the provided value to the normal
    private void addNormal(int vertIndex, float[] verts, float x, float y, float z) {

        int i = getNormalStart(vertIndex);

        verts[i] += x;
        verts[i + 1] += y;
        verts[i + 2] += z;
    }

    /*
     * Normalizes normals
     */
    private void normalizeNormal(int vertIndex, float[] verts) {

        int i = getNormalStart(vertIndex);

        float x = verts[i];
        float y = verts[i + 1];
        float z = verts[i + 2];

        float num2 = ((x * x) + (y * y)) + (z * z);
        float num = 1f / (float) Math.sqrt(num2);
        x *= num;
        y *= num;
        z *= num;

        verts[i] = x;
        verts[i + 1] = y;
        verts[i + 2] = z;
    }

    /*
     * Calculates the normals
     */
    private void calcNormals(short[] indices, float[] verts) {

        for (int i = 0; i < indices.length; i += 3) {
            int i1 = getPositionStart(indices[i]);
            int i2 = getPositionStart(indices[i + 1]);
            int i3 = getPositionStart(indices[i + 2]);

            // p1
            float x1 = verts[i1];
            float y1 = verts[i1 + 1];
            float z1 = verts[i1 + 2];

            // p2
            float x2 = verts[i2];
            float y2 = verts[i2 + 1];
            float z2 = verts[i2 + 2];

            // p3
            float x3 = verts[i3];
            float y3 = verts[i3 + 1];
            float z3 = verts[i3 + 2];

            // u = p3 - p1
            float ux = x3 - x1;
            float uy = y3 - y1;
            float uz = z3 - z1;

            // v = p2 - p1
            float vx = x2 - x1;
            float vy = y2 - y1;
            float vz = z2 - z1;

            // n = cross(v, u)
            float nx = (vy * uz) - (vz * uy);
            float ny = (vz * ux) - (vx * uz);
            float nz = (vx * uy) - (vy * ux);

            // normalize(n)
            float num2 = ((nx * nx) + (ny * ny)) + (nz * nz);
            float num = 1f / (float) Math.sqrt(num2);
            nx *= num;
            ny *= num;
            nz *= num;

            addNormal(indices[i], verts, nx, ny, nz);
            addNormal(indices[i + 1], verts, nx, ny, nz);
            addNormal(indices[i + 2], verts, nx, ny, nz);
        }

        for (int i = 0; i < (verts.length / vertexSize); i++) {
            normalizeNormal(i, verts);
        }
    }

}
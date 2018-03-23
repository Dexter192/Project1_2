package gameEngine;
public class CollisionTester 
{
	public static void main(String[] args)
	{
		BoundingSphere bir = new BoundingSphere(5, 0, 0, 0);
		//BoundingSphere iki = new BoundingSphere(5, 0, 0, 0);
		//AABB uc = new AABB(0, 0, 0, 3, 3, 3);
		AABB dort = new AABB(5, 5, 5, 15, 15, 15);
		//boolean alive = bir.intersectingBS(iki);
		//boolean another = uc.intersectingAABB(dort);
		boolean andAgain = dort.intersectingBS(bir);
		//System.out.println("Bir and iki' intersection's existance is " + alive);
		//System.out.println("Uc and dort's intersection's existance is " + another);
//		System.out.println("Bir and dort's intersection's existance is " + andAgain);
		andAgain = bir.intersectingAABB(dort);
//		System.out.println("Bir and dort's intersection's existance is " + andAgain);
	}
}

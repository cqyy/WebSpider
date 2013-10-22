package cn.edu.uestc.wsc.bbs_360;

/*
 * 管理需要提取的版块id*/
public class BlockManger {
	private static int[] block=new int[]{92,93};
	private static int index=0;
	
	public static int next(){
		int result= block[index];
		index=(index+1)%block.length;
		return result;
	}
}

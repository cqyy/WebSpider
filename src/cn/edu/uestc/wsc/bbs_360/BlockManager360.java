package cn.edu.uestc.wsc.bbs_360;

import cn.edu.uestc.wsc.bbs.BlockManager;

/*
 * 管理需要提取的版块id*/
public class BlockManager360 implements BlockManager {
	/*360论坛版块号*/
	private static int[] block=new int[]{92,93,1483,163,1648,162,
		166,167,171,170,169,1793,1862,101,
		102,1490,1190,177,67,69,70,71,137,
		226,217,218,213,214,216,215,84,78,
		73,80,81,1167,1133,1053,1078,1009,
		254,665,672,984,212,236,79,1612,74,
		75,77,172,173,174,1720,110,1664,1662,
		1665,1866,1254,151,150,152,693,1488,
		104,103,94,96,223,915,255,121,696};
	private static int index=0;
	
	public synchronized int next(){
		int result= block[index];
		index=(index+1)%block.length;
		return result;
	}
	@Override
	public int blockSize() {
		return block.length;
	}	
}

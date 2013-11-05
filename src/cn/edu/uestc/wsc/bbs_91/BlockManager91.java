package cn.edu.uestc.wsc.bbs_91;

import cn.edu.uestc.wsc.bbs.BlockManager;

public class BlockManager91 implements BlockManager {

	private static int[] block=new int[]{849,95,848,974};
	private static int index=0;
	@Override
	public int next() {
		int result= block[index];
		index=(index+1)%block.length;
		return result;
	}

	@Override
	public int blockSize() {
		return block.length;
	}

}

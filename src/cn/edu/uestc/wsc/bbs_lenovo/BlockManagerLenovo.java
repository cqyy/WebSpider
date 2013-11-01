package cn.edu.uestc.wsc.bbs_lenovo;

import cn.edu.uestc.wsc.bbs.BlockManager;

public class BlockManagerLenovo implements BlockManager {
	private static int index=0;
	private static int[] block=new int[]{45,2,37,38,39,40,41,44,46,47,48};

	@Override
	public synchronized int next() {
		int result= block[index];
		index=(index+1)%block.length;
		return result;
	}

	@Override
	public int blockSize() {
		return block.length;
	}

}

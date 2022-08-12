package com.a3.lottery.domain;

public class TrxBlock {
	private  String blockID;
	private TrxBlockHeader block_header;
	public String getBlockID() {
		return blockID;
	}
	public void setBlockID(String blockID) {
		this.blockID = blockID;
	}
	public TrxBlockHeader getBlock_header() {
		return block_header;
	}
	public void setBlock_header(TrxBlockHeader block_header) {
		this.block_header = block_header;
	}
	
	
	
	
}

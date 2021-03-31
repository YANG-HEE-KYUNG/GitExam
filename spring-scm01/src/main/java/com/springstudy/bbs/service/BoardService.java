package com.springstudy.bbs.service;

import java.util.Map;

import com.springstudy.bbs.domain.Board;

public interface BoardService {
	
	public abstract Map<String, Object> boardList(
			int pageNum, String type, String keyword);
	
	public abstract Board getBoard(int no, boolean isCount);
	
	public abstract void insertBoard(Board board);

	public boolean isPassCheck(int no, String pass);
	
	public abstract void updateBoard(Board board);
	
	public abstract void deleteBoard(int no);
}

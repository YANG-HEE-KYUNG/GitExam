package com.springstudy.bbs.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springstudy.bbs.domain.Board;

@Repository
public class BoardDaoImpl implements BoardDao {

	private final String NAME_SPACE = "com.springstudy.bbs.mapper.BoardMapper";
	
	private SqlSessionTemplate sqlSession;

	@Autowired
	public void setSqlSession(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}	
	
	@Override
	public List<Board> boardList(
			int startRow, int num, String type, String keyword) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startRow", startRow);
		params.put("num", num);
		params.put("type", type);
		params.put("keyword", keyword);		

		return sqlSession.selectList(NAME_SPACE + ".boardList", params);
	}
	
	@Override
	public int getBoardCount(String type, String keyword) {
		
		Map<String, String> params = new HashMap<String, String>();		
		params.put("type", type);
		params.put("keyword", keyword);
		
		return sqlSession.selectOne(NAME_SPACE + ".getBoardCount", params);
	}
	
	@Override
	public Board getBoard(int no, boolean isCount) {
		
		if(isCount) {
			sqlSession.update(NAME_SPACE + ".incrementReadCount", no);
		}
				 
		return sqlSession.selectOne(NAME_SPACE + ".getBoard", no);
	}

	@Override
	public void insertBoard(Board board) {
		
		sqlSession.insert(NAME_SPACE + ".insertBoard", board);
	}
	
	public boolean isPassCheck(int no, String pass) {	

		boolean result = false;
				
		String dbPass = sqlSession.selectOne(
				NAME_SPACE + ".isPassCheck",	no);

		if(dbPass.equals(pass)) {
			result = true;		
		}
		return result;
	}
	
	@Override
	public void updateBoard(Board board) {
		
		sqlSession.update(NAME_SPACE + ".updateBoard", board);
	}

	@Override
	public void deleteBoard(int no) {
		
		sqlSession.delete(NAME_SPACE + ".deleteBoard", no);
	}
}

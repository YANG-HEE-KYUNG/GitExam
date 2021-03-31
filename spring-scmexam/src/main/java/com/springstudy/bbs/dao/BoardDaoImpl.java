package com.springstudy.bbs.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springstudy.bbs.domain.Board;

// 이 클래스가 데이터 액세스(데이터 저장소) 계층의 컴포넌트(Bean) 임을 선언한다.
@Repository
public class BoardDaoImpl implements BoardDao {

	private final String NAME_SPACE = "com.springstudy.bbs.mapper.BoardMapper";
	
	private SqlSessionTemplate sqlSession;

	@Autowired
	public void setSqlSession(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}	
	
	// 한 페이지에 보여 질 게시 글 리스트와 검색 리스트 요청 시 호출되는 메소드
	@Override
	public List<Board> boardList(
			int startRow, int num, String type, String keyword) {
		
		// SQL 파라미터가 여러 개일 경우 Map을 이용하여 지정한다.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startRow", startRow);
		params.put("num", num);
		params.put("type", type);
		params.put("keyword", keyword);		

		return sqlSession.selectList(NAME_SPACE + ".boardList", params);
	}
	
	// 게시 글 수를 계산하기 위해 호출되는 메서드 - paging 처리에 사용
	@Override
	public int getBoardCount(String type, String keyword) {
		
		// SQL 파라미터가 여러 개일 경우 Map을 이용해 지정하면 된다.
		Map<String, String> params = new HashMap<String, String>();		
		params.put("type", type);
		params.put("keyword", keyword);
		
		return sqlSession.selectOne(NAME_SPACE + ".getBoardCount", params);
	}
	
	// 게시 글 상세보기 요청 시 호출되는 메서드
	@Override
	public Board getBoard(int no, boolean isCount) {
		
		// 게시 글 상세보기 요청만 게시 글 읽은 횟수를 증가시킨다.
		if(isCount) {
			sqlSession.update(NAME_SPACE + ".incrementReadCount", no);
		}
		
		// getBoard 맵핑 구문을 호출하면서 게시 글 번호인 no를 파라미터로 지정했다.		 
		return sqlSession.selectOne(NAME_SPACE + ".getBoard", no);
	}

	// 게시 글쓰기 요청 시 호출되는 메서드
	@Override
	public void insertBoard(Board board) {
		
		// insertBoard 맵핑 구문을 호출하면서 Board 객체를 파라미터로 지정했다.
		sqlSession.insert(NAME_SPACE + ".insertBoard", board);
	}
	
	// 게시 글 수정, 삭제 시 비밀번호 입력을 체크하는 메서드
	public boolean isPassCheck(int no, String pass) {	

		boolean result = false;
		
		// isPassCheck 맵핑 구문을 호출하면서 게시 글 번호인 no를 파라미터로 지정했다.
		String dbPass = sqlSession.selectOne(
				NAME_SPACE + ".isPassCheck",	no);

		// 비밀번호가 맞으면 true가 반환된다.
		if(dbPass.equals(pass)) {
			result = true;		
		}
		return result;
	}
	
	// 게시 글 수정 요청 시 호출되는 메서드
	@Override
	public void updateBoard(Board board) {
		
		// updateBoard 맵핑 구문을 호출하면서 Board 객체를 파라미터로 지정했다.
		sqlSession.update(NAME_SPACE + ".updateBoard", board);
	}

	// 게시 글 삭제 요청 시 호출되는 메서드
	@Override
	public void deleteBoard(int no) {
		
		// deleteBoard 맵핑 구문을 호출하면서 no를 파라미터로 지정했다.
		sqlSession.delete(NAME_SPACE + ".deleteBoard", no);
	}
}

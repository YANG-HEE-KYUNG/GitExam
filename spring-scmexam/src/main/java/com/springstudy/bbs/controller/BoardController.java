package com.springstudy.bbs.controller;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springstudy.bbs.domain.Board;
import com.springstudy.bbs.service.BoardService;

// commit 수정사항 확인하기
@Controller
public class BoardController {	

	@Autowired
	private BoardService boardService;
	
	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}	

	@RequestMapping(value= {"/boardList", "/list"})
	public String boardList(Model model, 
			@RequestParam(value="pageNum", required=false, 
						defaultValue="1") int pageNum,
			@RequestParam(value="type", required=false,  
						defaultValue="null") String type,
			@RequestParam(value="keyword", required=false,
						defaultValue="null") String keyword) {		
	
		Map<String, Object> modelMap = 
				boardService.boardList(pageNum, type, keyword);
		
		model.addAllAttributes(modelMap);		
		
		return "boardList";
	}
	
	@RequestMapping("/boardDetail")
	public String boardDetail(Model model, int no, 
			@RequestParam(value="pageNum", required=false, 
					defaultValue="1") int pageNum,
			@RequestParam(value="type", required=false,  
					defaultValue="null") String type,
			@RequestParam(value="keyword", required=false,
					defaultValue="null") String keyword) throws Exception {
		
		boolean searchOption = (type.equals("null") 
				|| keyword.equals("null")) ? false : true; 		
		
		Board board = boardService.getBoard(no, true);
		
		model.addAttribute("board", board);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("searchOption", searchOption);
		
		if(searchOption) {	
			model.addAttribute("keyword", URLEncoder.encode(keyword, "utf-8"));
			model.addAttribute("type", type);
			model.addAttribute("word", keyword);
		}

		return "boardDetail";
	}
	
	
	@RequestMapping(value="/writeProcess", method=RequestMethod.POST)
	public String insertBoard(Board board) {
		
		boardService.insertBoard(board);			
	
		return "redirect:boardList";
	}
	
	@RequestMapping(value="/update")
	public String updateBoard(Model model, HttpServletResponse response, 
			PrintWriter out, int no, String pass,
			@RequestParam(value="pageNum", required=false, 
					defaultValue="1") int pageNum,
			@RequestParam(value="type", required=false,  
					defaultValue="null") String type,
			@RequestParam(value="keyword", required=false,
					defaultValue="null") String keyword) throws Exception {
		
		boolean result = boardService.isPassCheck(no, pass);
		
		if(! result) {
			response.setContentType("text/html; charset=utf-8");				
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않습니다.');");
			out.println("	history.back();");
			out.println("</script>");

			return null;
		}		

		boolean searchOption = (type.equals("null") 
				|| keyword.equals("null")) ? false : true; 

		Board board = boardService.getBoard(no, false);

		model.addAttribute("board", board);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("searchOption", searchOption);
		
		if(searchOption) {
			model.addAttribute("keyword", URLEncoder.encode(keyword, "utf-8"));
			model.addAttribute("type", type);
			model.addAttribute("word", keyword);
		}

		return "updateForm";
	}
	
	@RequestMapping(value="update", method=RequestMethod.POST)
	public String updateBoard(HttpServletResponse response, 
			PrintWriter out, Board board,
			RedirectAttributes reAttrs, 
			@RequestParam(value="pageNum", required=false, 
					defaultValue="1") int pageNum,
			@RequestParam(value="type", required=false,  
					defaultValue="null") String type,
			@RequestParam(value="keyword", required=false,
					defaultValue="null") String keyword) throws Exception {		
		 
		boolean result = boardService.isPassCheck(board.getNo(), board.getPass());
		
		if(! result) {

			response.setContentType("text/html; charset=utf-8");				
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않습니다.');");
			out.println("	history.back();");
			out.println("</script>");

			return null;
		}

		boolean searchOption = (type.equals("null") 
				|| keyword.equals("null")) ? false : true; 
		
		boardService.updateBoard(board);		

		reAttrs.addAttribute("searchOption", searchOption);
		
		if(searchOption) {				
		
			reAttrs.addAttribute("keyword", keyword);
			reAttrs.addAttribute("type", type);
		}
		
		reAttrs.addAttribute("pageNum", pageNum);		
	
		return "redirect:boardList";
	}
	
	@RequestMapping({"/delete", "deleteBoard"})
	public String deleteBoard(HttpServletResponse response, 
			PrintWriter out, int no, String pass,
			RedirectAttributes reAttrs, 
			@RequestParam(value="pageNum", required=false, 
				defaultValue="1") int pageNum,
			@RequestParam(value="type", required=false,  
				defaultValue="null") String type,
			@RequestParam(value="keyword", required=false,
				defaultValue="null") String keyword) throws Exception {
		 
		boolean result = boardService.isPassCheck(no, pass);
		
		if(! result) {
			response.setContentType("text/html; charset=utf-8");				
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않습니다.');");
			out.println("	history.back();");
			out.println("</script>");

			return null;
		}

		boolean searchOption = (type.equals("null") 
				|| keyword.equals("null")) ? false : true; 
		
		boardService.deleteBoard(no);		

		reAttrs.addAttribute("searchOption", searchOption);
		
		if(searchOption) {	
			reAttrs.addAttribute("keyword", keyword);
			reAttrs.addAttribute("type", type);
		}
		
		reAttrs.addAttribute("pageNum", pageNum);
		
		return "redirect:boardList";
	}	
}

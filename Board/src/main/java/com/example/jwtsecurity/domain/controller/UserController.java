package com.example.jwtsecurity.domain.controller;


import com.example.jwtsecurity.domain.dto.BoardDTO;
import com.example.jwtsecurity.domain.dto.CommentDTO;
import com.example.jwtsecurity.domain.dto.MemberDTO;
import com.example.jwtsecurity.domain.entity.Token;
import com.example.jwtsecurity.domain.service.*;
import com.example.jwtsecurity.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;
    private final MemberService memberService;
    private final BoardService boardService;
    private final LikeService likeService;
    private final CommentService commentService;

    @PostMapping("/join")
    public MemberDTO memberDTO(@RequestBody MemberDTO memberDTO) {
        log.info("회원가입 시도됨");
        System.out.println("memberDTO = " + memberDTO);
        return memberService.join(memberDTO);

    }


    @PostMapping("/login")
    public Token login(@RequestBody MemberDTO memberDTO) {
        log.info("user name = {}", memberDTO.getName());
        boolean response = memberService.login(memberDTO);
        System.out.println("response = " + response);
        if (response) {
            Token tokenDto = jwtTokenProvider.createToken(memberDTO.getName());
            log.info("getusername = {}", memberDTO.getName());
            System.out.println("tokenDto = " + tokenDto);
            jwtService.login(tokenDto);
            return tokenDto;
        }
        return null;
    }


    @PostMapping("/register")
    public BoardDTO boardDTO(@RequestBody BoardDTO boardDTO) {
        log.info("board context = {}", boardDTO.getTexts());
        boardService.register(boardDTO);
        return boardDTO;
    }

    @PostMapping("/comment")
    public CommentDTO commentDTO(@RequestBody CommentDTO commentDTO) {
        commentService.commentRegister(commentDTO);
        return commentDTO;
    }

    @PostMapping("/board/{boardId}")
    public BoardDTO boardPage(@PathVariable Long boardId, Model model) {
        BoardDTO boardDTO = boardService.getBoard(boardId);
        model.addAttribute("board", boardDTO);
        System.out.println("얻어낸 보드 = " + boardDTO);
        return boardDTO;
    }

    @PostMapping("/board/{boardId}/like")
    public BoardDTO boardLike(@PathVariable Long boardId, Model model) {
        BoardDTO boardDTO = likeService.getLike(boardId);
        model.addAttribute("board", boardDTO);
        System.out.println("얻어낸 보드 = " + boardDTO);
        return boardDTO;
    }


}

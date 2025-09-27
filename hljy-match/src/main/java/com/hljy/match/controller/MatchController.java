package com.hljy.match.controller;

import com.hljy.common.result.Result;
import com.hljy.match.service.MatchService;
import com.hljy.match.vo.MatchVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 匹配控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
@Tag(name = "匹配管理", description = "匹配相关接口")
public class MatchController {
    
    private final MatchService matchService;

}

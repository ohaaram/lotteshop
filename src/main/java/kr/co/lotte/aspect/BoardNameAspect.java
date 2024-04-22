package kr.co.lotte.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class BoardNameAspect {
    /*
      private final ConfigRepository configRepository;




    @Pointcut("execution(* kr.co.farmstory.controller.ArticleController.*(..))")
    public void boardNameAttribute(){}

    @AfterReturning(pointcut = "boardNameAttribute() && args(model, cate, ..)")
    public void addBoardName(Model model, String cate){
        log.info("addBoardName!!!");
        Optional<Config> optConfig = configRepository.findById(cate);
        optConfig.ifPresent(config -> {
            String boardName = config.getBoardName();
            if (boardName != null && !boardName.isEmpty()) {
                model.addAttribute("boardName", boardName);
            }
        });
    }

     */
}
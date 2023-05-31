package com.sea.common.exception;

import com.sea.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLException.class)
    public R<String> exceptionHandler(SQLException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
    @ExceptionHandler(MyException.class)
    public R<String> exceptionHandler(MyException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }


}

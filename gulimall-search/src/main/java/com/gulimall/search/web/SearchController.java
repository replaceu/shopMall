package com.gulimall.search.web;

import com.gulimall.search.service.MallSearchService;
import com.gulimall.search.vo.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author aqiang9  2020-08-19
 */
@Controller
public class SearchController {
    @Autowired
    MallSearchService mallSearchService ;

    @GetMapping({"/","list.html"})
    public String searchIndex(SearchParam searchParam){
        return "list";
    }
}
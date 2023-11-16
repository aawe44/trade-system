package com.jason.trade.web.portal;

import com.jason.trade.web.portal.service.StaticPageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StaticPageTest {

    @Autowired
    private StaticPageService staticPageService;

    @Test
    public void createStaticPageTest() {
        staticPageService.createStaticPage(9);
    }
}

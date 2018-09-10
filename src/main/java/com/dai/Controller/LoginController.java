package com.dai.Controller;

import com.dai.pojo.TbUser;
import com.dai.util.CookieUtils;
import com.dai.util.E3Result;
import com.dai.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录的Controller
 *
 * @author adrain
 */
@Controller
public class LoginController {

    @RequestMapping("/login/toLogin")
    public String showLogin() {
        return "login";
    }

    @RequestMapping(value = "/login/login", method = RequestMethod.POST)
    public String login(TbUser user, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (user != null) {
                if ("root".equals(user.getUsername()) && "root".equals(user.getPassword())) {
                    user.setPassword("");
                    request.getSession().setAttribute("root", user);
                    response.sendRedirect("http://localhost:8081/");
                } else if ("other".equals(user.getUsername()) && "123456".equals(user.getPassword())) {
                    user.setPassword("");
                    request.getSession().setAttribute("other", user);
                    response.sendRedirect("http://localhost:8081/index-other");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "login";
    }

}

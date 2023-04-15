package com.plant.content.api;

import com.plant.base.util.AjaxResult;
import com.plant.base.util.QRCodeUtil;
import com.plant.base.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

/**
 * @description 二维码控制器
 */
@Controller
public class QrcodeController {
    /**
     * @description 生成二维码
     * @param  request
     * @Param  response
     * @return  void
     **/
    int i=0;
    @GetMapping("/getQrcode")
    @ResponseBody
    public void getQrcode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //二维码中的链接，需要公网网址才可以用手机扫描出来，本地测试开通natapp进行内网渗透
        String url = "http://localhost:8080/scanQrcode";
        //过期时间，30s
        long expireTime = 30;
        //设置参数
        // random = request.getParameter("random");
        //String userId = request.getParameter("userId");
        //生成二维码唯一标识
        String key = String.valueOf(System.currentTimeMillis());
        //设置二维码过期时间
       // redisUtils.set(key,random,expireTime);
        //二维码中的内容
        String content = url;
        //二维码图片中间logo
        String imgPath = null;
        Boolean needCompress = true;
        //拿到图片流
        ByteArrayOutputStream out = QRCodeUtil.encodeIO(content, imgPath, needCompress);
        //返回二维码
        response.setCharacterEncoding("UTF-8");
        response.setContentType("image/jpeg;charset=UTF-8");
        response.setContentLength(out.size());
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(out.toByteArray());
        outputStream.flush();
        outputStream.close();
    }

    @GetMapping("/scanQrcode")
    @ResponseBody
    public String scanQrcode(HttpServletRequest request, String key, String userId) throws Exception {

            return "扫描成功";
    }

    @RequestMapping("/confirmQrcode")
    @ResponseBody
    public AjaxResult confirmQrcode(String userId){
            return AjaxResult.success("扫描成功", "/success");
    }
 

    @GetMapping("/success")
    public  String success(ModelMap modelMap){
        return "success";
    }
 
}
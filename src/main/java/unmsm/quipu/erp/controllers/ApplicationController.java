package unmsm.quipu.erp.controllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;

@Controller
@Scope("session")
public class ApplicationController {
    @Autowired
    private UserService userService;



    public String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();
            map.addAttribute("userDetails", userDetails);
        }
        return "index";
    }


    @RequestMapping(value = "/adminis/{token}", method = RequestMethod.GET)
    public String index(@PathVariable(value = "token") String token, ModelMap model, final HttpServletRequest request)
            throws IOException, JSONException {

        //read info from Google APIs
        String[] parts = request.getRequestURI().split("/");
        token = parts[parts.length - 1];
        JSONObject json = readJsonFromUrl("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + token);

        //para pasar parametros por get addAttribute
//        model.addAttribute("email",(String)json.get("email"));

        //authentication
        String email= (String) json.get("email");
        int pos=email.indexOf("@unmsm.edu.pe");
//        if(servidorservice.Existe_servidor((String) json.get("email"))&(pos>0)){
            Authentication result = userService.login((String) json.get("email"));
            return "redirect:/admin";
//        }else{
//            return "fail";
//        }
    }

    @RequestMapping(value = "/perfiles/{token}", method = RequestMethod.GET)
    public String perfil(@PathVariable(value = "token") String token, ModelMap model, final HttpServletRequest request)
            throws IOException, JSONException {

        //read info from Google APIs
        String[] parts = request.getRequestURI().split("/");
        token = parts[parts.length - 1];
        JSONObject json = readJsonFromUrl("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + token);

        //para pasar parametros por get addAttribute
        // model.addAttribute("email",(String)json.get("email"));
        System.out.println("entro perfiles");
        //authentication
        String email= (String) json.get("email");
        int pos=email.indexOf("@unmsm.edu.pe");
        if(pos>0){
            /*if(!servidorservice.Existe_histusu(email)){
                servidorservice.insertUsuPerfil(email);
            }*/
           // Authentication result = userService.login((String) json.get("email"));
            //return "redirect:/perfil";
            return "perfil";
        }else{
            return "fail";
        }
    }

    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(ModelMap map) {
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<? extends GrantedAuthority> securedMessage = userService.getAuthorities(userDetails);
        map.addAttribute("userDetails", userDetails);
        map.addAttribute("userAuthorities", securedMessage);
        return "admin";
    }

    @RequestMapping(value = "/perfil", method = RequestMethod.GET)
    public String page_perfil(ModelMap map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();
            map.addAttribute("userDetails", userDetails);
        }
        return "perfil";
    }
}

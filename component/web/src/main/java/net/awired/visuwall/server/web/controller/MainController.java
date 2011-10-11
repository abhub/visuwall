/**
 *     Copyright (C) 2010 Julien SMADJA <julien dot smadja at gmail dot com> - Arnaud LEMAIRE <alemaire at norad dot fr>
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package net.awired.visuwall.server.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.awired.ajsl.web.domain.JsServiceMap;
import net.awired.ajsl.web.service.CssService;
import net.awired.ajsl.web.service.JsService;
import net.awired.ajsl.web.service.JsonService;
import net.awired.visuwall.core.business.service.WallHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

@Controller
public class MainController {

    private static final String ROOT_CONTEXT = "/index.html";

    //    @Autowired
    //    VisuwallApplication visuwallApplication;

    @Autowired
    CssService cssService;

    @Autowired
    JsService jsService;

    @Autowired
    JsonService jsonService;

    @Autowired
    WallHolderService wallService;

    @RequestMapping(ROOT_CONTEXT)
    public ModelAndView getIndex(ModelMap modelMap) throws Exception {
        Map<String, Object> jsData = new HashMap<String, Object>();
        Map<String, Object> init = new HashMap<String, Object>();
        jsData.put("init", init);

        Map<File, String> jsMap = jsService.getJsFiles();
        JsServiceMap serviceMap = jsService.buildServiceMapFromJsMap(jsMap);
        Predicate<List<String>> predicate = new Predicate<List<String>>() {
            @Override
            public boolean apply(List<String> value) {
                for (String string : value) {
                    return string.startsWith("visuwall");
                }
                return true;
            }
        };
        Map<String, List<String>> val = Maps.filterValues(serviceMap, predicate);
        Map<String, List<String>> serviceMethods = jsService.getServicesMethods(jsMap, val);

        jsData.put("jsServiceMethod", serviceMethods);
        jsData.put("jsService", val);
        init.put("wallNames", wallService.getWallNames());

        modelMap.put("jsData", jsonService.serialize(jsData));
        modelMap.put("jsLinks", jsService.getJsLinks("res/", jsMap));
        modelMap.put("cssLinks", cssService.getCssLinks("res/"));
        //TODO
        //        modelMap.put("version", visuwallApplication.getVersion());
        return new ModelAndView("index", modelMap);
    }

    @RequestMapping("/")
    public void getSlash(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestDispatcher dispatcher = request.getRequestDispatcher(ROOT_CONTEXT);
        dispatcher.forward(request, response);
    }
}

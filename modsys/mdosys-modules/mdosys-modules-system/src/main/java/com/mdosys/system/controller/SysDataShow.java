package com.mdosys.system.controller;

import com.mdosys.common.core.enums.Paraminfo;
import com.mdosys.common.core.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mdosys.common.core.utils.file.ReadNormalFile.readParams;

@RestController
@RequestMapping("/dataShow")
public class SysDataShow {

    @PostMapping(value = "/singleFile")
    public AjaxResult getParamFromFile(HttpServletRequest request) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        List<List<String>> lists = new ArrayList<>();
        if (files.size() > 0){
            byte[] bytes = files.get(0).getBytes();
            InputStream in = new ByteArrayInputStream(bytes);
            List<Paraminfo> list = readParams(in);
            for(Paraminfo p: list){
                if (p.getSign().equals("%ComArray")){
                    String values = p.getValue();
                    String names = p.getColumnNames();
                    List<String> name = new ArrayList<>(Arrays.asList(names.split("\\s+")));
                    lists.add(name);
                    List<String> value = new ArrayList<>(Arrays.asList(values.split("\\r\\n")));
                    for (String s: value){
                        List<String> list1 = new ArrayList<>(Arrays.asList(s.split("\\s+")));
                        lists.add(list1);
                    }
                    break;
                }
            }
        }
        return AjaxResult.success(lists);
    }
}

package com.mdosys.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/projects/temp")
public class TempController {

    @GetMapping(value = "/log")
    public String getLog(@PathVariable long id){
        return "[LOG-PATH]: /opt/apache-dolphinscheduler-2.0.1-bin/logs/7529656050144_4/10/26.log, [HOST]:  172.17.0.8\n" +
                "[INFO] 2022-11-13 22:07:20.936  - [taskAppId=TASK-7529656050144_4-10-26]:[83] - shell task params {\"resourceList\":[{\"id\":1,\"res\":\"fa_dong_ji_hou_chu_li.zip\",\"resourceName\":\"/fa_dong_ji_hou_chu_li.zip\"}],\"localParams\":[],\"rawScript\":\"unzip -o -j fa_dong_ji_hou_chu_li.zip\\njava -jar app_nowin.jar\",\"dependence\":{},\"conditionResult\":{\"successNode\":[],\"failedNode\":[]},\"waitStartTimeout\":{},\"switchResult\":{}}\n" +
                "[INFO] 2022-11-13 22:07:20.939  - [taskAppId=TASK-7529656050144_4-10-26]:[137] - raw script : unzip -o -j fa_dong_ji_hou_chu_li.zip\n" +
                "java -jar app_nowin.jar\n" +
                "[INFO] 2022-11-13 22:07:20.939  - [taskAppId=TASK-7529656050144_4-10-26]:[138] - task execute path : /tmp/dolphinscheduler/exec/process/7494642584288/7529656050144_4/10/26\n" +
                "[INFO] 2022-11-13 22:07:20.939  - [taskAppId=TASK-7529656050144_4-10-26]:[86] - tenantCode user:root, task dir:10_26\n" +
                "[INFO] 2022-11-13 22:07:20.940  - [taskAppId=TASK-7529656050144_4-10-26]:[91] - create command file:/tmp/dolphinscheduler/exec/process/7494642584288/7529656050144_4/10/26/10_26.command\n" +
                "[INFO] 2022-11-13 22:07:20.940  - [taskAppId=TASK-7529656050144_4-10-26]:[117] - command : #!/bin/sh\n" +
                "BASEDIR=$(cd `dirname $0`; pwd)\n" +
                "cd $BASEDIR\n" +
                "export JAVA_HOME=/usr/local/openjdk-8\n" +
                "export PATH=$JAVA_HOME/bin:$PATH\n" +
                "/tmp/dolphinscheduler/exec/process/7494642584288/7529656050144_4/10/26/10_26_node.sh\n" +
                "[INFO] 2022-11-13 22:07:20.941  - [taskAppId=TASK-7529656050144_4-10-26]:[285] - task run command: sudo -u root sh /tmp/dolphinscheduler/exec/process/7494642584288/7529656050144_4/10/26/10_26.command\n" +
                "[INFO] 2022-11-13 22:07:20.943  - [taskAppId=TASK-7529656050144_4-10-26]:[176] - process start, process id is: 3272\n" +
                "[INFO] 2022-11-13 22:07:21.944  - [taskAppId=TASK-7529656050144_4-10-26]:[61] -  -> welcome to use bigdata scheduling system...\n" +
                "\tArchive:  fa_dong_ji_hou_chu_li.zip\n" +
                "\t  inflating: app.jar                 \n" +
                "\t  inflating: app_nowin.jar           \n" +
                "\t  inflating: ballistic.txt           \n" +
                "\t  inflating: index_extract.txt       \n" +
                "\t  inflating: mass.txt                \n" +
                "\t  inflating: match.txt               \n" +
                "\t  inflating: output.txt              \n" +
                "\tjava.lang.NullPointerException\n" +
                "\t\tat sun.awt.FontConfiguration.getVersion(FontConfiguration.java:1264)\n" +
                "\t\tat sun.awt.FontConfiguration.readFontConfigFile(FontConfiguration.java:219)\n" +
                "\t\tat sun.awt.FontConfiguration.init(FontConfiguration.java:107)\n" +
                "\t\tat sun.awt.X11FontManager.createFontConfiguration(X11FontManager.java:774)\n" +
                "\t\tat sun.font.SunFontManager$2.run(SunFontManager.java:441)\n" +
                "\t\tat java.security.AccessController.doPrivileged(Native Method)\n" +
                "\t\tat sun.font.SunFontManager.<init>(SunFontManager.java:386)\n" +
                "\t\tat sun.awt.FcFontManager.<init>(FcFontManager.java:35)\n" +
                "\t\tat sun.awt.X11FontManager.<init>(X11FontManager.java:57)\n" +
                "\t\tat sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)\n" +
                "\t\tat sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)\n" +
                "\t\tat sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)\n" +
                "\t\tat java.lang.reflect.Constructor.newInstance(Constructor.java:423)\n" +
                "\t\tat java.lang.Class.newInstance(Class.java:442)\n" +
                "\t\tat sun.font.FontManagerFactory$1.run(FontManagerFactory.java:83)\n" +
                "\t\tat java.security.AccessController.doPrivileged(Native Method)\n" +
                "\t\tat sun.font.FontManagerFactory.getInstance(FontManagerFactory.java:74)\n" +
                "\t\tat sun.font.SunFontManager.getInstance(SunFontManager.java:250)\n" +
                "\t\tat sun.font.FontDesignMetrics.getMetrics(FontDesignMetrics.java:264)\n" +
                "\t\tat sun.swing.SwingUtilities2.getFontMetrics(SwingUtilities2.java:1125)\n" +
                "\t\tat javax.swing.JComponent.getFontMetrics(JComponent.java:1626)\n" +
                "\t\tat javax.swing.text.PlainView.calculateLongestLine(PlainView.java:639)\n" +
                "\t\tat javax.swing.text.PlainView.updateMetrics(PlainView.java:209)\n" +
                "\t\tat javax.swing.text.PlainView.updateDamage(PlainView.java:527)\n" +
                "\t\tat javax.swing.text.PlainView.insertUpdate(PlainView.java:451)\n" +
                "\t\tat javax.swing.text.FieldView.insertUpdate(FieldView.java:293)\n" +
                "\t\tat javax.swing.plaf.basic.BasicTextUI$RootView.insertUpdate(BasicTextUI.java:1610)\n" +
                "\t\tat javax.swing.plaf.basic.BasicTextUI$UpdateHandler.insertUpdate(BasicTextUI.java:1869)\n" +
                "\t\tat javax.swing.text.AbstractDocument.fireInsertUpdate(AbstractDocument.java:201)\n" +
                "\t\tat javax.swing.text.AbstractDocument.handleInsertString(AbstractDocument.java:748)\n" +
                "\t\tat javax.swing.text.AbstractDocument.insertString(AbstractDocument.java:707)\n" +
                "\t\tat javax.swing.text.PlainDocument.insertString(PlainDocument.java:130)\n" +
                "\t\tat javax.swing.text.AbstractDocument.replace(AbstractDocument.java:669)\n" +
                "\t\tat javax.swing.text.JTextComponent.setText(JTextComponent.java:1669)\n" +
                "\t\tat javax.swing.plaf.metal.MetalComboBoxEditor$1.setText(MetalComboBoxEditor.java:61)\n" +
                "\t\tat javax.swing.plaf.basic.BasicComboBoxEditor.setItem(BasicComboBoxEditor.java:87)\n" +
                "\t\tat javax.swing.JComboBox.setSelectedItem(JComboBox.java:572)\n" +
                "\t\tat nudt.ui.MatchPanel.initData(MatchPanel.java:586)\n" +
                "\t\tat nudt.ui.MatchPanel.initialize(MatchPanel.java:560)\n" +
                "\t\tat nudt.ui.MatchPanel.<init>(MatchPanel.java:531)\n" +
                "\t\tat nudt.ui.MainPanel.<init>(MainPanel.java:61)\n" +
                "\t\tat nudt.ui.MainCal.main(MainCal.java:32)\n" +
                "[INFO] 2022-11-13 22:07:22.407  - [taskAppId=TASK-7529656050144_4-10-26]:[200] - process has exited, execute path:/tmp/dolphinscheduler/exec/process/7494642584288/7529656050144_4/10/26, processId:3272 ,exitStatusCode:0 ,processWaitForStatus:true ,processExitValue:0\n" +
                "[INFO] 2022-11-13 22:07:22.945  - [taskAppId=TASK-7529656050144_4-10-26]:[55] - FINALIZE_SESSION\n";
    }
}

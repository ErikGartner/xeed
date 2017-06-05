/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

/**
 * @author Erik
 */
public class HTMLTemplate {

    /*
     * HTML export template
     */
    public static final String HTML_TEMPLATE_HEADER = "<html>\n"
            + "<head>\n"
            + "<meta http-equiv=\"content-type\" content=\"text/html;charset=UTF-8\" />\n"
            + "<title>%XEED_TITLE%</title>\n"
            + "<script type='text/javascript'>\n"
            + "function toggle_visibility(tbid,lnkid)\n"
            + "{\n"
            + "if(document.all){document.getElementById(tbid).style.display = document.getElementById(tbid).style.display == 'block' ? 'none' : 'block';}\n"
            + "else{document.getElementById(tbid).style.display = document.getElementById(tbid).style.display == 'table' ? 'none' : 'table';}\n"
            + "document.getElementById(lnkid).value = document.getElementById(lnkid).value == '[-] Collapse' ? '[+] Expand' : '[-] Collapse';\n"
            + "}\n" + "</script>\n" + "<style type='text/css'>\n" + "%XEED_TABLE_ID_LIST% {display:none;}\n"
            + "%XEED_LINK_ID_LIST% {border:none;background:none;width:85px;}\n"
            + "td {FONT-SIZE: 75%; MARGIN: 0px; COLOR: #000000;}\n"
            + "td {FONT-FAMILY: verdana,helvetica,arial,sans-serif}\n" + "a {TEXT-DECORATION: none;}\n" + "h2#cp {"
            + "font-family: times, Times New Roman, times-roman, georgia, serif;" + "color: #5E5E5E;" + "margin: 0;"
            + "padding: 0px 0px px 0px;" + "font-size: 51px;" + "line-height: 44px;" + "letter-spacing: -2px;"
            + "font-weight: bold;" + "text-transform: none;" + "}\n" + "</style>\n" + "</head>\n" + "<body>\n"
            + "<center><h2 id='cp'>%XEED_TITLE%</h2><center><br>\n";
    public static final String HTML_TEMPLATE_TITLE_TAG = "%XEED_TITLE%";
    public static final String HTML_TEMPLATE_TABLE_ID_LIST = "%XEED_TABLE_ID_LIST%";
    public static final String HTML_TEMPLATE_LINK_ID_LIST = "%XEED_LINK_ID_LIST%";
    public static final String HTML_TEMPLATE_TABLE_HEADER_ROW = "<table width='55%' border='0' align='center' cellpadding='4' cellspacing='0'>\n"
            + "<tr height='1'>\n"
            + "<td bgcolor='#727272' colspan='2'></td>\n"
            + "</tr>\n"
            + "<tr bgcolor='#EEEEEE' height='15'>\n"
            + "<td><strong>%XEED_CHARACTER_NAME%</strong></td>\n"
            + "<td bgcolor='#EEEEEE' align='right'><input id='%XEED_TABLE_LINK_ID%' type='button' value='[+] Expand' onclick=\"toggle_visibility('%XEED_TABLE_ID%','%XEED_TABLE_LINK_ID%');\"></td>\n"
            + "</tr>\n"
            + "<td colspan='2'>\n"
            + "<table width='90%' align='center' border='0' cellpadding='4' cellspacing='0' id='%XEED_TABLE_ID%'>\n";
    public static final String HTML_TEMPLATE_CHARACTER_NAME_TAG = "%XEED_CHARACTER_NAME%";
    public static final String HTML_TEMPLATE_TABLE_ID = "%XEED_TABLE_ID%";
    public static final String HTML_TEMPLATE_EXPAND_LINK_ID = "%XEED_TABLE_LINK_ID%";
    public static final String HTML_TEMPLATE_TABLE_ROW = "<tr>\n"
            + "<td width='20%' valign='top'>%XEED_ITEM_NAME%</td>\n" + "<td width='80%'>%XEED_ITEM_DATA%</td>\n"
            + "</tr>\n" + "<tr height='1'>\n" + "<td colspan='2' bgcolor='#CCCCCC'></td>\n" + "</tr>\n";
    public static final String HTML_TEMPLATE_TABLE_ITEM_NAME = "%XEED_ITEM_NAME%";
    public static final String HTML_TEMPLATE_TABLE_ITEM_DATA = "%XEED_ITEM_DATA%";
    public static final String HTML_TEMPLATE_FOOTER_ROW = "<tr height='1'>\n"
            + "<td colspan='2' bgcolor='#CCCCCC'></td>\n" + "</tr>\n" + "<tr height='1'>\n"
            + "<td bgcolor='#CCCCCC' colspan='2'></td>\n" + "</tr>\n" + "<tr height='1'>\n"
            + "<td bgcolor='#727272' colspan='2'></td>\n" + "</tr>\n" + "<tr height='8'>\n" + "<td colspan='2'></td>\n"
            + "</tr>\n" + "</table>\n" + "</td>\n" + "</tr>\n" + "</table>\n";
    public static final String HTML_TEMPLATE_FOOTER = "<p><center><i>%XEED_FOOTER_TEXT%</i></center></p>" + "</body>\n"
            + "</html>\n";
    public static final String HTML_TEMPLATE_FOOTER_TEXT = "%XEED_FOOTER_TEXT%";

    public static final String HTML_TEMPLATE_FAMILY_TREE = "<div>\n" +
            "  <script src=\"https://d3js.org/d3.v4.min.js\"></script>\n" +
            "  <script src=\"https://cdn.jsdelivr.net/lodash/4.17.4/lodash.min.js\"></script>\n" +
            "  <script src=\"https://cdn.rawgit.com/ErikGartner/dTree/2.0.2/dist/dTree.min.js\"></script>\n" +
            "  <div id=\"graph\"></div>\n" +
            "\n" +
            "  <style>\n" +
            "    .linage {\n" +
            "        fill: none;\n" +
            "        stroke: black;\n" +
            "    }\n" +
            "    .marriage {\n" +
            "        fill: none;\n" +
            "        stroke: black;\n" +
            "    }\n" +
            "    .node {\n" +
            "        background-color: white;\n" +
            "        border-style: solid;\n" +
            "        border-width: 1px;\n" +
            "    }\n" +
            "    .nodeText{\n" +
            "        font: 10px sans-serif;\n" +
            "    }\n" +
            "    svg {\n" +
            "      border: 1px solid black;\n" +
            "    }\n" +
            "  </style>\n" +
            "  <script>\n" +
            "    treeData = %TREE_DATA%\n" +
            "  </script>\n" +
            "\n" +
            "  <script>\n" +
            "    dTree.init(treeData, {width: window.innerWidth * 55 / 100,\n" +
            "                          height: window.innerHeight * 3 / 4});\n" +
            "  </script>\n" +
            "</div>";

    public static final String HTML_TEMPLATE_FAMILY_TREE_DATA = "%TREE_DATA%";
}

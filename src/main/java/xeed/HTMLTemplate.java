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
    public static final String HTML_TEMPLATE_HEADER = "<!DOCTYPE html>\n" +
            "<html lang=\"en\" dir=\"ltr\">\n" +
            "  <head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>%XEED_TITLE%</title>\n" +
            "    <script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\"></script>\n" +
            "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.3.3/semantic.min.js\"></script>\n" +
            "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.3.3/semantic.min.css\" />\n" +
            "  </head>\n" +
            "  <body>\n" +
            "    <div style=\"padding-top:1em\" class=\"ui container\">\n" +
            "      <h1 style=\"font-size:4rem;\" class=\"ui centered header\">\n" +
            "        <div class=\"content\">\n" +
            "          %XEED_TITLE%\n" +
            "        </div>\n" +
            "      </h1>\n" +
            "      <div class=\"ui centered doubling stackable four column grid\">\n";
    public static final String HTML_TEMPLATE_TITLE_TAG = "%XEED_TITLE%";
    public static final String HTML_TEMPLATE_TABLE_HEADER_ROW = "      <div class=\"column\"><div class=\"ui fluid card\">\n" +
            "%XEED_IMAGE_FIELD%\n" +
            "          <div class=\"content\">\n" +
            "            <div class=\"center aligned header\">\n" +
            "              %XEED_CHARACTER_NAME%\n" +
            "            </div>\n" +
            "            <div class=\"description\">\n" +
            "              <div class=\"ui list\">";
    public static final String HTML_TEMPLATE_CHARACTER_NAME_TAG = "%XEED_CHARACTER_NAME%";
    public static final String HTML_TEMPLATE_TABLE_ROW = "<div class=\"item\">\n" +
            "                  <div class=\"content\">\n" +
            "                    <div class=\"header\">%XEED_ITEM_NAME%</div>\n" +
            "                    <div class=\"description\">%XEED_ITEM_DATA%</div>\n" +
            "                  </div>\n" +
            "                </div>";
    public static final String HTML_TEMPLATE_TABLE_ITEM_NAME = "%XEED_ITEM_NAME%";
    public static final String HTML_TEMPLATE_TABLE_ITEM_DATA = "%XEED_ITEM_DATA%";
    public static final String HTML_TEMPLATE_IMAGE_FIELD_TAG = "%XEED_IMAGE_FIELD%";
    public static final String HTML_TEMPLATE_IMAGE_FIELD = "          <div class=\"ui image\">\n" +
            "            <img src=\"%XEED_ITEM_DATA%\">\n" +
            "          </div>";
    public static final String HTML_TEMPLATE_FOOTER_ROW = "              </div>\n" +
            "            </div>\n" +
            "          </div>\n" +
            "        </div></div>";
    public static final String HTML_TEMPLATE_FOOTER = "      </div>\n" +
            "  </body>\n" +
            "\n" +
            "  <footer>\n" +
            "    <div class=\"ui container\">\n" +
            "      <div class=\"ui center aligned basic segment\">\n" +
            "        %XEED_FOOTER_TEXT%\n" +
            "      </div>\n" +
            "    </div>\n" +
            "  </footer>\n" +
            "</html>\n";
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
            "      border-top: 1px solid black; border-bottom: 1px solid black;\n" +
            "    }\n" +
            "  </style>\n" +
            "  <script>\n" +
            "    treeData = %TREE_DATA%\n" +
            "  </script>\n" +
            "\n" +
            "  <script>\n" +
            "    dTree.init(treeData, {width: window.innerWidth,\n" +
            "                          height: window.innerHeight * 3 / 4});\n" +
            "  </script>\n" +
            "</div>";

    public static final String HTML_TEMPLATE_FAMILY_TREE_DATA = "%TREE_DATA%";
}

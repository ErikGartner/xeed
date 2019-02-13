/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.LayeredIcon;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.*;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Erik
 */
public class GenealogyGraphPanel extends javax.swing.JPanel {

    private final int ImageMaxSize = 32;
    Forest<CharacterNode, Integer> graph;
    Factory<DirectedGraph<CharacterNode, Integer>> graphFactory = new Factory<DirectedGraph<CharacterNode, Integer>>() {

        @Override
        public DirectedGraph<CharacterNode, Integer> create() {
            return new DirectedSparseMultigraph<CharacterNode, Integer>();
        }
    };
    Factory<Tree<CharacterNode, Integer>> treeFactory = new Factory<Tree<CharacterNode, Integer>>() {

        @Override
        public Tree<CharacterNode, Integer> create() {
            return new DelegateTree<CharacterNode, Integer>(graphFactory);
        }
    };
    Factory<Integer> edgeFactory = new Factory<Integer>() {

        int i = 0;

        @Override
        public Integer create() {
            return i++;
        }
    };
    VisualizationViewer<CharacterNode, Integer> vv;
    TreeLayout<CharacterNode, Integer> treeLayout;
    CharacterNode[] characters;

    public GenealogyGraphPanel() {
        initComponents();
    }

    public void InitiateGraph(Character[] cs, Template[] templates, Color[] colors, String[] picture_keys,
            String[] extra_keys) {

        Map<CharacterNode, Icon> iconMap = new HashMap<CharacterNode, Icon>();

        characters = new CharacterNode[cs.length];
        for (int x = 0; x < cs.length; x++) {

            characters[x] = new CharacterNode(cs[x]);

            for (int y = 0; y < templates.length; y++) {

                if (cs[x].templateIdentifier.equals(templates[y].GetTemplateID())) {

                    characters[x].color = colors[y];
                    characters[x].extra_key = extra_keys[y];

                    ImageIcon img = new ImageIcon(XEED.RescaleImage(((ImageIcon) cs[x].imgData.get(picture_keys[y])).getImage(), ImageMaxSize,
                            ImageMaxSize, false));

                    if (img != null) {
                        iconMap.put(characters[x], new LayeredIcon(img.getImage()));
                    }

                }

            }
        }

        // creates the graph
        graph = new DelegateForest<CharacterNode, Integer>();

        //Loads the tree data
        createTree();

        //Sets up the transformer that paints the node.
        Transformer<CharacterNode, Paint> vertexPaint = new Transformer<CharacterNode, Paint>() {

            @Override
            public Paint transform(CharacterNode cn) {
                ;
                return cn.color;
            }
        };

        final DefaultVertexIconTransformer<CharacterNode> vertexIconFunction = new DefaultVertexIconTransformer<CharacterNode>();
        vertexIconFunction.setIconMap(iconMap);

        final VertexIconShapeTransformer<CharacterNode> vertexImageShapeFunction = new VertexIconShapeTransformer<CharacterNode>(
                new EllipseVertexShapeTransformer<CharacterNode>());
        vertexImageShapeFunction.setIconMap(iconMap);

        treeLayout = new TreeLayout<CharacterNode, Integer>(graph, TreeLayout.DEFAULT_DISTX * 3);

        //Skriva en anpassad metod för att bestämma platser.
        //använd treeLayout.setLocation();
        vv = new VisualizationViewer<CharacterNode, Integer>(treeLayout);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setVertexShapeTransformer(vertexImageShapeFunction);
        vv.getRenderContext().setVertexIconTransformer(vertexIconFunction);
        // add a listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller());
        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.AUTO);

        Container content = this;
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        content.add(panel);
        panel.setVisible(true);
        content.validate();

        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        vv.setGraphMouse(graphMouse);

        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

    }

    private void createTree() {

        for (int x = 0; x < characters.length; x++) {

            Character c = characters[x].character;

            if (c.chrData.isEmpty()) {

                graph.addVertex(characters[x]);
                _printchildren(characters[x]);

            }
        }

    }

    private void _printchildren(CharacterNode cn) {

        for (int x = 0; x < characters.length; x++) {

            if (characters[x].character.chrData.containsValue(cn.character)) {
                graph.addEdge(edgeFactory.create(), cn, characters[x]);
                _printchildren(characters[x]);
            }

        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    class CharacterNode {

        public Color color = Color.RED;
        public String extra_key = "";
        Character character;

        public CharacterNode(Character c) {
            this.character = c;
        }

        @Override
        public String toString() {

            String extra = "";
            if (character.szData.get(extra_key) != null) {
                String s = (String) character.szData.get(extra_key);
                if (!s.isEmpty()) {
                    extra += " (" + s + ")";
                }
            }

            return character.GetCharacterName() + extra;

        }
    }
}

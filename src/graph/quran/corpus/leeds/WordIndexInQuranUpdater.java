package graph.quran.corpus.leeds;

import base.GraphIndices;
import base.Importer;
import base.NodeProperties;
import model.api.chapter.Chapter;
import model.api.verse.Verse;
import model.api.word.Word;
import model.impl.word.WordImpl;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.xml.sax.SAXException;
import util.NodeUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by vahidoo on 3/18/15.
 */
public class WordIndexInQuranUpdater implements Importer {
    @Override
    public void doImport(GraphDatabaseService database) throws ParserConfigurationException, IOException, SAXException {

        try (Transaction tx = database.beginTx()) {
            int index = 1;

            Node node = database.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, "1:1:1").getSingle();
            Word current = new WordImpl(node);

            while (current != null) {

                node = ((WordImpl) current).getNode();
                NodeUtils.setPropertyAndIndex(node, NodeProperties.Word.indexInQuran, GraphIndices.WordIndex, index);

                System.out.println(current.getAddress() + "-->" + index);

                current = nextWord(current);


                index++;

            }

            tx.success();
        }

    }

    private Word nextWord(Word current) {
        Word successor = current.getSuccessor();
        if (successor != null) {
            return successor;
        }

        Verse nextVerse = current.getVerse().getSuccessor();

        if (nextVerse != null)
            return nextVerse.getWord(1);

        Chapter nextChapter = current.getVerse().getChapter().getNextChapter();
        if (nextChapter != null) {
            return nextChapter.getVerse(1).getWord(1);
        }


        return null;
    }

}
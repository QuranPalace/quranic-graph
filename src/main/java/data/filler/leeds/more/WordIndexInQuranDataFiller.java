package data.filler.leeds.more;

import data.schema.GraphIndices;
import data.schema.NodeProperties;
import data.filler.DataFiller;
import data.filler.TransactionalFiller;
import model.api.chapter.Chapter;
import model.api.verse.Verse;
import model.api.word.Word;
import model.impl.base.ManagersSet;
import model.impl.word.WordImpl;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.xml.sax.SAXException;
import util.NodeUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by vahidoo on 3/18/15.
 */
public class WordIndexInQuranDataFiller extends DataFiller {

    @Override
    protected List<TransactionalFiller> getTransactionalFillers() throws Throwable {
        List<TransactionalFiller> fillers = new ArrayList<>();
        fillers.add(new WordIndexFiller());
        return fillers;
    }

    public WordIndexInQuranDataFiller(GraphDatabaseService database, ManagersSet managersSet, Properties properties) {
        super(database, managersSet, properties);
    }


    private class WordIndexFiller implements TransactionalFiller {
        @Override
        public void fillInTransaction(GraphDatabaseService database) throws ParserConfigurationException, IOException, SAXException {
            int index = 1;

            Node node = database.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, "1:1:1").getSingle();
            Word current = managersSet.getSession().get(Word.class, node);

            while (current != null) {

                node = ((WordImpl) current).getNode();
                NodeUtils.setPropertyAndIndex(node, NodeProperties.Word.indexInQuran, GraphIndices.WordIndex, index);

                current = nextWord(current);

                index++;

            }
        }

        private Word nextWord(Word current) {

            System.out.println("Update chapter #" + current.getVerse().getChapter().getIndex());

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
}

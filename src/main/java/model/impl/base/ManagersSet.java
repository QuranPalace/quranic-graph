package model.impl.base;

import model.api.base.Factory;
import model.api.base.Session;
import model.api.chapter.Chapter;
import model.api.lemma.Lemma;
import model.api.root.Root;
import model.api.root.RootManager;
import model.api.token.Token;
import model.api.token.TokenManager;
import model.api.verse.Verse;
import model.api.verse.VerseManager;
import model.api.word.Word;
import model.impl.chapter.ChapterFactory;
import model.impl.lemma.LemmaFactory;
import model.impl.root.RootFactory;
import model.impl.root.RootManagerImpl;
import model.impl.token.TokenFactory;
import model.impl.token.TokenManagerImpl;
import model.impl.verse.VerseFactory;
import model.impl.verse.VerseManagerImpl;
import model.impl.word.WordFactory;
import org.neo4j.graphdb.GraphDatabaseService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vahidoo on 3/13/15.
 */
public class ManagersSet {
    private static Map<GraphDatabaseService, ManagersSet> map = new HashMap<>();

    private Session session;
    private RootManager rootManager;
    private VerseManager verseManager;
    private TokenManager tokenManager;


    public ManagersSet(GraphDatabaseService database) {

        session = new MapSession();
        HashMap<Class<?>, Factory> factories = new HashMap<>();
        factories.put(Chapter.class, new ChapterFactory(session));
        factories.put(Verse.class, new VerseFactory(session));
        factories.put(Word.class, new WordFactory(session));
        factories.put(Token.class, new TokenFactory(session));
        factories.put(Root.class, new RootFactory(session));
        factories.put(Lemma.class, new LemmaFactory(session));
        ((MapSession) session).setFactories(factories);

        rootManager = new RootManagerImpl(session, database);
        verseManager = new VerseManagerImpl(session, database);
        tokenManager = new TokenManagerImpl(session, database);
    }

    public RootManager getRootManager() {
        return rootManager;
    }

    public VerseManager getVerseManager() {
        return verseManager;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public Session getSession() {
        return session;
    }
}

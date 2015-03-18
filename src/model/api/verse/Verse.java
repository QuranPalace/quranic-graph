package model.api.verse;

import model.api.base.NodeContainer;
import model.api.chapter.Chapter;
import model.api.word.Word;

/**
 * Created by vahidoo on 3/11/15.
 */
public interface Verse extends NodeContainer {

    Object getAddress();
    Chapter getChapter();

    Integer getIndex();

    Word getWord(int i);

    Verse getSuccessor();
}

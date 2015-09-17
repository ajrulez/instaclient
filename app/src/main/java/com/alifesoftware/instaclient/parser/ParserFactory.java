package com.alifesoftware.instaclient.parser;

import com.alifesoftware.instaclient.interfaces.IPopularImageParser;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * Demonstration of Factory pattern - Not really needed
 * for this assignment, but I just added it to show how
 * we can have multiple parsers for the same type of data
 * when either theformat of the data is different or when
 * we want to use different mechanism for parsing
 *
 * Hope this doesn't backfire :-)
 *
 */
public class ParserFactory {
    // Enum for Parser Types in case we want to support different Parser
    public static enum ParserType {
        PARSER_INSTAGRAM_JSON, // Uses standard JSON library
        PARSER_INSTAGRAM_GSON // In case I have time and I want to implement a Gson Based Parser as well
    }

    /**
     * Creates a parser to parse the Popular Picture response
     * based on the parameters
     *
     * @param type
     * @return
     */
    public static IPopularImageParser createParser(ParserType type) {
        IPopularImageParser parser = null;
        switch(type) {
            case PARSER_INSTAGRAM_JSON: {
                parser = new InstagramPopularPictureParserJson();
            }
            break;

            case PARSER_INSTAGRAM_GSON: {
                parser = null; // If I have time and I implement a Gson Based Parser, then create the object here
            }
            break;

            default:
                // Nothing to do
        }

        return parser;
    }
}

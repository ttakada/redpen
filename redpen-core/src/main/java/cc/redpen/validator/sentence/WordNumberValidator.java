/**
 * redpen: a text inspection tool
 * Copyright (C) 2014 Recruit Technologies Co., Ltd. and contributors
 * (see CONTRIBUTORS.md)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.redpen.validator.sentence;

import cc.redpen.RedPenException;
import cc.redpen.ValidationError;
import cc.redpen.config.SymbolTable;
import cc.redpen.config.ValidatorConfiguration;
import cc.redpen.model.Sentence;
import cc.redpen.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Validate input sentences have more words than specified.
 */
public class WordNumberValidator implements Validator<Sentence> {
    /**
     * Default maximum number of words in one sentence.
     */
    @SuppressWarnings("WeakerAccess")
    public static final int DEFAULT_MAXIMUM_WORDS_IN_A_SENTENCE = 30;
    private static final Logger LOG =
            LoggerFactory.getLogger(WordNumberValidator.class);
    private int maxWordNumber;

    public WordNumberValidator() {
        super();
        this.maxWordNumber = DEFAULT_MAXIMUM_WORDS_IN_A_SENTENCE;
    }

    public WordNumberValidator(ValidatorConfiguration config,
                               SymbolTable symbolTable)
            throws RedPenException {
        initialize(config);
    }

    public List<ValidationError> validate(Sentence sentence) {
        List<ValidationError> result = new ArrayList<>();
        String content = sentence.content;
        String[] wordList = content.split(" ");
        int wordNum = wordList.length;
        if (wordNum > maxWordNumber) {
            result.add(new ValidationError(
                    this.getClass(),
                    "The number of the words exceeds the maximum "
                            + String.valueOf(wordNum), sentence
            ));
        }
        return result;
    }

    private boolean initialize(
            ValidatorConfiguration conf)
            throws RedPenException {
        if (conf.getAttribute("max_word_num") == null) {
            this.maxWordNumber = DEFAULT_MAXIMUM_WORDS_IN_A_SENTENCE;
            LOG.info("max_length was not set.");
            LOG.info("Using the default value of max_length.");
        } else {
            this.maxWordNumber = Integer.valueOf(conf.getAttribute("max_word_num"));
        }
        return true;
    }
}
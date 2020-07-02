package com.gustavolessa.csvcomparisontool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;

/**
 * Validates the arguments provided.
 */
public class ArgsValidator {
    private static final Logger LOG = LoggerFactory.getLogger(ArgsValidator.class);

    private ApplicationArguments args;

    public ArgsValidator(ApplicationArguments args) {
        this.args = args;
    }

    public boolean isValid(){
        System.out.println(args.getNonOptionArgs());
        System.out.println(args.getOptionNames());
        System.out.println(args.getSourceArgs());
        return true;
    }

}

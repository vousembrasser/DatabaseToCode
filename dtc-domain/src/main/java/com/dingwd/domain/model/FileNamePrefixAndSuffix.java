package com.dingwd.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileNamePrefixAndSuffix {

    private String preFix;
    private String middle;
    private String suffix;
    private String fileSuffix = GeneratorInfo.INSTANCE.getFileSuffix();

    public FileNamePrefixAndSuffix preFix(String preFix) {
        this.preFix = preFix;
        return this;
    }

    public FileNamePrefixAndSuffix middle(String middle) {
        this.middle = middle;
        return this;
    }

    public FileNamePrefixAndSuffix suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public FileNamePrefixAndSuffix fileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
        return this;
    }


}

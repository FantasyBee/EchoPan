package com.echopan.entity.dto;

import java.io.Serializable;

public class UserSpaceDto implements Serializable {
    private Long useSpace;
    private Long totalSpace;

    public Long getUseSpace() {
        return useSpace;
    }

    public void setUseSpace(Long useSpace) {
        this.useSpace = useSpace;
    }

    public Long getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(Long totalSpace) {
        this.totalSpace = totalSpace;
    }
}

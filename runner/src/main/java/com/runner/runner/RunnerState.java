package com.runner.runner;

import java.util.UUID;

public class RunnerState {

    private UUID runnerId;
    private boolean registered;

    public UUID getRunnerId() {
        return runnerId;
    }

    public void setRunnerId(UUID runnerId) {
        this.runnerId = runnerId;
        this.registered = true;
    }

    public boolean isRegistered() {
        return registered;
    }
}

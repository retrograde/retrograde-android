package com.codebutler.retrograde.ipc;

interface IRetrogradeIpcService {

    void writeToLog(int priority, String tag, String message, String error);
}

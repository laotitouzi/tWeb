package com.ss.httpsession;

import java.util.Observer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @author 寮犲崕
 *
 */
public interface RequestEventObserver{
  public void completed(HttpServletRequest req, HttpServletResponse res);
}
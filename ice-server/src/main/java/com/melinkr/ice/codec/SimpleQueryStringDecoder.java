package com.melinkr.ice.codec;

import io.netty.handler.codec.http.HttpConstants;

import java.nio.charset.Charset;
import java.util.*;

/**
 * <pre>不支持同一参数提交多个值简易QueryStringDecoder</pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public class SimpleQueryStringDecoder {

    private final Charset charset;
    private final String uri;
    private final boolean hasPath;
    private final int maxParams;
    private String path;
    private Map<String, String> params;
    private int nParams;

    public SimpleQueryStringDecoder(String uri) {
        this(uri, HttpConstants.DEFAULT_CHARSET);
    }


    public SimpleQueryStringDecoder(String uri, Charset charset) {
        this(uri, charset, true);
    }

    public SimpleQueryStringDecoder(String uri, Charset charset, boolean hasPath) {
        this(uri, charset, hasPath, 1024);
    }

    public SimpleQueryStringDecoder(String uri, Charset charset, boolean hasPath, int maxParams) {
        if(uri == null) {
            throw new NullPointerException("getUri");
        } else if(charset == null) {
            throw new NullPointerException("charset");
        } else if(maxParams <= 0) {
            throw new IllegalArgumentException("maxParams: " + maxParams + " (expected: a positive integer)");
        } else {
            this.uri = uri;
            this.charset = charset;
            this.maxParams = maxParams;
            this.hasPath = hasPath;
        }
    }

    public String uri() {
        return this.uri;
    }

    public String path() {
        if(this.path == null) {
            if(!this.hasPath) {
                this.path = "";
            } else {
                int pathEndPos = this.uri.indexOf(63);
                this.path = decodeComponent(pathEndPos < 0?this.uri:this.uri.substring(0, pathEndPos), this.charset);
            }
        }

        return this.path;
    }

    public Map<String, String> parameters() {
        if(this.params == null) {
            if(this.hasPath) {
                int pathEndPos = this.uri.indexOf(63);
                if(pathEndPos >= 0 && pathEndPos < this.uri.length() - 1) {
                    this.decodeParams(this.uri.substring(pathEndPos + 1));
                } else {
                    this.params = Collections.emptyMap();
                }
            } else if(this.uri.isEmpty()) {
                this.params = Collections.emptyMap();
            } else {
                this.decodeParams(this.uri);
            }
        }

        return this.params;
    }

    private void decodeParams(String s) {
        Map params = this.params = new LinkedHashMap();
        this.nParams = 0;
        String name = null;
        int pos = 0;

        int i;
        for(i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if(c == 61 && name == null) {
                if(pos != i) {
                    name = decodeComponent(s.substring(pos, i), this.charset);
                }

                pos = i + 1;
            } else if(c == 38 || c == 59) {
                if(name == null && pos != i) {
                    if(!this.addParam(params, decodeComponent(s.substring(pos, i), this.charset), "")) {
                        return;
                    }
                } else if(name != null) {
                    if(!this.addParam(params, name, decodeComponent(s.substring(pos, i), this.charset))) {
                        return;
                    }

                    name = null;
                }

                pos = i + 1;
            }
        }

        if(pos != i) {
            if(name == null) {
                this.addParam(params, decodeComponent(s.substring(pos, i), this.charset), "");
            } else {
                this.addParam(params, name, decodeComponent(s.substring(pos, i), this.charset));
            }
        } else if(name != null) {
            this.addParam(params, name, "");
        }

    }

    private boolean addParam(Map<String, String> params, String name, String value) {
        if(this.nParams >= this.maxParams) {
            return false;
        } else {
            params.put(name, value);
            ++this.nParams;
            return true;
        }
    }

    public static String decodeComponent(String s) {
        return decodeComponent(s, HttpConstants.DEFAULT_CHARSET);
    }

    public static String decodeComponent(String s, Charset charset) {
        if(s == null) {
            return "";
        } else {
            int size = s.length();
            boolean modified = false;

            for(int buf = 0; buf < size; ++buf) {
                char pos = s.charAt(buf);
                if(pos == 37 || pos == 43) {
                    modified = true;
                    break;
                }
            }

            if(!modified) {
                return s;
            } else {
                byte[] var9 = new byte[size];
                int var10 = 0;

                for(int i = 0; i < size; ++i) {
                    char c = s.charAt(i);
                    switch(c) {
                        case '%':
                            if(i == size - 1) {
                                throw new IllegalArgumentException("unterminated escape sequence at end of string: " + s);
                            }

                            ++i;
                            c = s.charAt(i);
                            if(c == 37) {
                                var9[var10++] = 37;
                                break;
                            } else {
                                if(i == size - 1) {
                                    throw new IllegalArgumentException("partial escape sequence at end of string: " + s);
                                }

                                c = decodeHexNibble(c);
                                ++i;
                                char c2 = decodeHexNibble(s.charAt(i));
                                if(c == '\uffff' || c2 == '\uffff') {
                                    throw new IllegalArgumentException("invalid escape sequence `%" + s.charAt(i - 1) + s.charAt(i) + "\' at index " + (i - 2) + " of: " + s);
                                }

                                c = (char)(c * 16 + c2);
                            }
                        default:
                            var9[var10++] = (byte)c;
                            break;
                        case '+':
                            var9[var10++] = 32;
                    }
                }

                return new String(var9, 0, var10, charset);
            }
        }
    }

    private static char decodeHexNibble(char c) {
        return 48 <= c && c <= 57?(char)(c - 48):(97 <= c && c <= 102?(char)(c - 97 + 10):(65 <= c && c <= 70?(char)(c - 65 + 10):'\uffff'));
    }
}

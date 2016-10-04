package be.cetic.rbac.man.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;

import org.apache.log4j.Logger;

public class RequestWrapper extends ServletRequestWrapper{

        private ServletInputStream wrappedInputStream;

    private static final Logger log = Logger.getLogger(RequestWrapper.class.getName());

        public RequestWrapper(ServletRequest request) throws IOException{

                super(request);

        }

        public String getData() throws IOException{
                StringBuffer buffer = new StringBuffer();;
                BufferedReader reader = getRequest().getReader();
                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                reader.close();
                String content = buffer.toString();
                final ByteArrayInputStream bis = new ByteArrayInputStream(content.getBytes("UTF-8"));
                wrappedInputStream = new ServletInputStream() {
                        @Override
                        public int read() throws IOException
                        {
                                return bis.read();
                        }
                };
                return content;
        }

        public ServletInputStream getInputStream() throws IOException {
                if (wrappedInputStream == null){
                        return getRequest().getInputStream();
                }
                else{
                        return wrappedInputStream;
                }
        }

}
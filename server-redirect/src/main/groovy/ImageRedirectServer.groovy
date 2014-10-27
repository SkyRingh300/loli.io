

import io.loli.sc.server.redirect.socket.RedirectFilter
import io.loli.storage.redirect.RedirectServer

import java.util.concurrent.Executors


port = 8888;
if(args.size() < 1){
    print("You may give args as port to listen. Now default port is" + 8888);
} else {
    args = args as List;
    args = args.unique();
    service = Executors.newCachedThreadPool();
    args.each({ port->
        service.submit({
            port = port as int;
            server = new RedirectServer().filter(new RedirectFilter()).address("0.0.0.0").port(port);
            server.start();
        } as Runnable);
    })
}

import io.loli.sc.server.redirect.socket.RedirectFilter
import io.loli.storage.redirect.RedirectServer

import java.util.concurrent.Executors


if(args.size() < 1){
    print("You should give at least one parameter as the port to listen");
} else {
    args = args as List;
    args = args.unique();
    service = Executors.newCachedThreadPool();
    args.each({port->
        service.submit({
            port = port as int;
            server = new RedirectServer().filter(new RedirectFilter()).port(port);
            server.start();
        } as Runnable);
    })
}
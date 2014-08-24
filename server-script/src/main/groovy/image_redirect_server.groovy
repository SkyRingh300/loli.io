import io.loli.sc.server.redirect.config.Config;
import io.loli.sc.server.redirect.socket.RedirectFilter;
import io.loli.storage.redirect.RedirectServer;

server = new RedirectServer().filter(new RedirectFilter()).port(Config.port);
server.start;
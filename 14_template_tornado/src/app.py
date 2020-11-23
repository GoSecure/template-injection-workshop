import tornado.template
import tornado.ioloop
import tornado.web


class MainHandler(tornado.web.RequestHandler):

    def get(self):
        self.render("index.html")

class SubscribeHandler(tornado.web.RequestHandler):

    def post(self):
        name = self.get_argument('name', '')
        template_data = """
Hello """+name+""",
Welcome to the {{company}} mailing-list.
        """
        t = tornado.template.Template(template_data)
        preview = t.generate(company="TornadeMedia")
        self.render("subscribe.html", preview=preview)

class My404Handler(tornado.web.RequestHandler):

    def prepare(self):
        self.set_status(404)
        self.render("404.html")

application = tornado.web.Application([
    (r"/", MainHandler),
    (r"/subscribe", SubscribeHandler),
    ], default_handler_class=My404Handler, debug=False, static_path='static', template_path='templates')
 
if __name__ == '__main__':
    application.listen(80, address='0.0.0.0')
    tornado.ioloop.IOLoop.instance().start()
from http.server import BaseHTTPRequestHandler, HTTPServer
import socketserver
import time
import json
from urllib.parse import parse_qs, urlparse
import threading
from datetime import datetime
import requests

hostName = "localhost"
serverPort = 8080



class CurrencyApi:
    def __init__(self) -> None:
        api_key = "&access_key=3p6PQYrYrFwyQ3FCaSag"
        base_api = "https://fcsapi.com/api-v3/forex/latest?"
        symbols = "symbol="
        #get_symbols_query = "https://fcsapi.com/api-v3/forex/list?type=forex" + api_key
        #get_symbols = requests.get(get_symbols_query).json()['response']
        
        #for i in range(int(len(get_symbols)*0.525)):
        #    symbols += get_symbols[i]['symbol'] + ','
        
        symbols_list = ['USD','EUR','GBP','ILS','CNY']
        
        pairs = ''
        for s1 in symbols_list:
            for s2 in symbols_list:
                if s1 != s2:
                    pairs += s1 + '/' + s2 + ','

        pairs = pairs[:-1]


        symbols = symbols + pairs
        self.query = base_api + symbols + api_key
        self.data = {}
        self.lock = threading.Lock()
    
    def update_data(self):
        while True:
            try:
                self.lock.acquire()
                data_list = requests.get(self.query).json()['response']
                for item in data_list:
                    self.data[item['s']] = (item['c'], item['cp'])
                self.lock.release()
                time.sleep(300)
            except Exception as e:
                print("Api Update error" + str(e))
                time.sleep(300)
                continue
        
    def run(self):
        threading.Thread(target= self.update_data, args=()).start()



class MyServer(BaseHTTPRequestHandler):

    def set_API(self, api: CurrencyApi):
        self.api = api


    def do_GET(self):
        self.send_response(200)
        self.send_header("Content-type", "application/json")
        self.end_headers()
        quert_components = parse_qs(urlparse(self.path).query)
        try:
            symbols = quert_components['symbol'][0]
            symbols = symbols.split(",")
            response = []
            for sym in symbols:
                if sym not in self.api.data.keys():
                    raise Exception("symbol : " + sym + "not found")
                response.append({"s" : sym, "c": self.api.data[sym][0], "cp": self.api.data[sym][1]})
            self.wfile.write(bytes(json.dumps(response),encoding='utf8'))
        except Exception as e:
            self.wfile.write(bytes(json.dumps({"Error": str(e), "time": str(datetime.now())}),encoding='utf8'))
              





if __name__ == "__main__":
    api = CurrencyApi()
    api.run()
    my_server = MyServer
    my_server.set_API(my_server,api=api)
    webServer = HTTPServer((hostName, serverPort), my_server)
    print("Server started http://%s:%s" % (hostName, serverPort))

    try:
        webServer.serve_forever()
    except KeyboardInterrupt:
        pass

    webServer.server_close()
    print("Server stopped.")
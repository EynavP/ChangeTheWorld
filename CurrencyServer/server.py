from http.server import BaseHTTPRequestHandler, HTTPServer
import socketserver
import time
import json
from typing import Tuple
from urllib import response
from urllib.parse import parse_qs, urlparse
import threading
from datetime import datetime
import requests
import urllib.request
from geopy.geocoders import Nominatim
import haversine as hs


external_ip = urllib.request.urlopen('https://ident.me').read().decode('utf8')
hostName = "0.0.0.0"
serverPort = 80



class LocationApi:
    def __init__(self) -> None:
        self.loc = Nominatim(user_agent="GetLoc")

    def CalculateDistance(self, address_1: str, address_2: str) -> float:
        getLoc1 = self.loc.geocode(address_1)
        getLoc2 = self.loc.geocode(address_2)
        loc1 = (getLoc1.latitude,getLoc1.longitude)
        loc2 = (getLoc2.latitude,getLoc2.longitude)
        return hs.haversine(loc1,loc2)
    
    def GetGeoloc(self, address: str) -> Tuple:
        getloc = self.loc.geocode(address)
        loc = (getloc.latitude, getloc.longitude)
        return loc




class AutoCompleteApi:
    def __init__(self) -> None:
        self.base_api = "https://api.peopledatalabs.com/v5/autocomplete?api_key=8ba1ec1ff087a1628acbb08cb5d01da63bda8fe00ab1c8f4a907d6bd7102e173&field=location&text="
        self.memmory = {}
        
    def GetComplition(self, query: str) -> list:
        if query not in self.memmory.keys():
            data = requests.get(self.base_api + query).json()
            data_list = data['data'][:5]
            tmp = []
            for d in data_list:
                tmp.append(d["name"].replace(",",""))
            self.memmory[query] = tmp
        
        return self.memmory[query]



class CurrencyApi:
    def __init__(self) -> None:
        api_key = "&access_key=GXRmNO2sPirxNQcg3gs1DmKfo"
        base_api = "https://fcsapi.com/api-v3/forex/latest?"
        symbols = "symbol="
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
                time.sleep(40000)
            except Exception as e:
                print("Api Update error" + str(e))
                time.sleep(40000)
                continue
        
    def run(self):
        threading.Thread(target= self.update_data, args=()).start()


class MyServer(BaseHTTPRequestHandler):

    def set_API(self, api_currency: CurrencyApi, api_location: LocationApi, api_autocomplete: AutoCompleteApi):
        self.api_currency = api_currency
        self.api_location = api_location
        self.api_autocomplete = api_autocomplete


    def do_GET(self):
        self.send_response(200)
        self.send_header("Content-type", "application/json")
        self.end_headers()
        query_components = parse_qs(urlparse(self.path).query)
        try:            
            if 'symbol' in query_components.keys():
                response = self.get_currency_data(query_components)
            
            elif 'geolocation' in query_components.keys():
                response = self.get_geolocation(query_components)

            elif 'location' in query_components.keys():
                response = self.get_location(query_components)
            
            elif 'autocomplete' in query_components.keys():
                response = self.api_autocomplete.GetComplition(query_components['autocomplete'][0])

            self.wfile.write(bytes(json.dumps(response),encoding='utf8'))
        except Exception as e:
            self.wfile.write(bytes(json.dumps({"Error": str(e), "time": str(datetime.now())}),encoding='utf8'))
              
    def get_currency_data(self, query_components) -> list:
            symbols = query_components['symbol'][0]
            symbols = symbols.split(",")
            response = []
            for sym in symbols:
                if sym not in self.api_currency.data.keys():
                    raise Exception("symbol : " + sym + "not found")
                response.append({"s" : sym, "c": self.api_currency.data[sym][0], "cp": self.api_currency.data[sym][1]})
            return response
    
    def get_location(self, query_components) -> list:
        address = query_components['location'][0]
        address_1 = address.split('/')[0]
        address_2 = address.split('/')[1]
        return [self.api_location.CalculateDistance(address_1=address_1, address_2=address_2)]
    
    def get_geolocation(self, query_components) -> list:
        address = query_components['geolocation'][0]
        print("here")
        geo_loc = self.api_location.GetGeoloc(address)
        return [geo_loc[0], geo_loc[1]]




if __name__ == "__main__":
    api_currency = CurrencyApi()
    api_location = LocationApi()
    api_autocomplete = AutoCompleteApi()
    api_currency.run()
    my_server = MyServer
    my_server.set_API(my_server,api_currency=api_currency, api_location=api_location, api_autocomplete=api_autocomplete)
    webServer = HTTPServer((hostName, serverPort), my_server)
    print(f"Server started http://{hostName}:{serverPort} or http://{external_ip}:{serverPort}") 

    try:
        webServer.serve_forever()
    except KeyboardInterrupt:
        pass

    webServer.server_close()
    print("Server stopped.")

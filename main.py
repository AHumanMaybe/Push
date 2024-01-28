# main.py
from typing import Union
from fastapi import FastAPI, Request, APIRouter
import pymongo
from bson.json_util import dumps
from bson.json_util import loads
from fastapi.templating import Jinja2Templates
from fastapi.responses import HTMLResponse, RedirectResponse
from fastapi.staticfiles import StaticFiles
from gptResponse import getGpt
import datetime
import secrets
import time

app = FastAPI()
app.mount("/img", StaticFiles(directory="img"), name="img")

templates = Jinja2Templates(directory="templates")
page_router = APIRouter()

@app.get("/")
async def root():
    return {"message" : "empty"}

@app.get("/login")
async def read_item(uid: str = "ffffffff"):
	myclient = pymongo.MongoClient("mongodb://127.0.0.1:27017")
	db = myclient["push"]
	col = db["users"]
	ret = col.find({"uid" : f'{uid}'}, {'_id': 0})
	return loads(dumps(ret))
	
@app.get("/leaderboard")
async def root():
	myclient = pymongo.MongoClient("mongodb://127.0.0.1:27017")
	db = myclient["push"]
	col = db["users"]
	ret = col.find({}, {'_id': 0}).sort({"points" : -1})
	print(ret)
	return loads(dumps(ret))

@app.get("/joingroup")
async def read_item(uid: str, q:Union[str, None] = None, group: str = "no"):
	myclient = pymongo.MongoClient("mongodb://127.0.0.1:27017")
	db = myclient["push"]
	col = db["users"]
	col.update_one({"uid" : f'{uid}'}, {"$set": {"group" : f'{group}'}})
	return {"message" : "success"}

@app.get("/verify")
async def static(request: Request):
	myclient = pymongo.MongoClient("mongodb://127.0.0.1:27017")
	
	db = myclient["push"]
	col = db["tasks"]
	ret = col.find({"verified" : 0}, {'_id' : 0}).sort({"time" : -1}).limit(1)
	obj = loads(dumps(ret))

	x = (len(obj) < 1)
	print(x)

	if(len(obj) < 1):
		return templates.TemplateResponse("index2.html", {"request": request})
	else:
		uid = obj[0]["uid"]

		print(len(obj))

		db2 = myclient["push"]
		col2 = db2["users"]
		ret2 = col2.find({"uid" : f'{uid}'}, {'_id': 0})
		obj2 = loads(dumps(ret2))
		print(uid)
		print(obj)
		print(obj2)
		uname = obj2[0]["name"]
		id = obj[0]["id"]
		print(datetime.datetime.fromtimestamp(obj[0]["time"]).strftime('%Y-%m-%d %H:%M:%S'))
		return templates.TemplateResponse("index.html", {"request": request ,"data" : obj[0], "time" : datetime.datetime.fromtimestamp(obj[0]["time"],).strftime('%m-%d-%Y %H:%M'), "name" : uname, "id": id})

@app.get("/inserttask")
async def read_item(uid: str, q:Union[str, None] = None, score: int = 5):

	ins_id = secrets.token_hex(3)
	
	task = { 
        "id": f'{ins_id}', 
        "uid": f'{uid}', 
        "score": score,
		"time": int(time.time()),
		"verified": 0
    } 

	myclient = pymongo.MongoClient("mongodb://127.0.0.1:27017")
	db = myclient["push"]
	col = db["tasks"]
	col.insert_one(task)
	return {"message" : "success"}

@app.get("/verifytask")
async def read_item(uid: str, q:Union[str, None] = None, id: str = "", verify: int = 0):

	myclient = pymongo.MongoClient("mongodb://127.0.0.1:27017")

	db = myclient["push"]
	col = db["tasks"]
	col.update_one({"id" : f'{id}'}, {"$set" : {"verified" : verify}})

	if(verify < 1):
		redirect = RedirectResponse(url = "/verify") 
		return redirect

	collection_clients = db["users"]
	collection_c_Tasks = db["tasks"]

	user_task_list = list(collection_c_Tasks.find({"uid":f'{uid}'}, {'_id' : 0}).sort({"time" : +1}))#Input User Found From Input
	user_task_newest = user_task_list[0]
	user_task_latest = user_task_list[1]
	user_id = list(user_task_newest.values())[1+1]
	current_user = list(collection_clients.find({"uid":f'{uid}'}, {'_id' : 0}))[0]

	#Check Streak
	if(list(current_user.values())[4] == 0):
		streak = 1
	else:
		last_time = list(user_task_latest.values())[2+1]
		new_task_time = list(user_task_newest.values())[2+1]
		if((new_task_time - last_time) > 86400):
			streak = 1
		else:
			streak = list(current_user.values())[4] +1
	
	#Get User Points, Streak, & Multipliers
	group = list(current_user.values())[2]
	points = list(current_user.values())[3]
	multipliers = list(current_user.values())[5]
	
	if(streak == 30):
		multipliers = 2
	if(streak == 180):
		multipliers = 5
	if(streak == 365):
		multipliers = 10
	if(streak <= 1):
		points = points + (multipliers * 5000)
	if(streak == 2):
		points = points + (multipliers * 6000)
	if(streak == 3):
		points = points + (multipliers * 7000)
	if(streak == 4):
		points = points + (multipliers * 8000)
	if(streak == 5):
		points = points + (multipliers * 9000)
	if(streak > 5):
		points = points + (multipliers * 10000)
	
	myquery = {"uid":f'{uid}'}
	newvalues = {"$set": {"points": points}}
	collection_clients.update_one(myquery, newvalues)
	
	newvalues = {"$set": {"streak": streak}}
	collection_clients.update_one(myquery, newvalues)
	
	newvalues = {"$set": {"multiplier": multipliers}}
	collection_clients.update_one(myquery, newvalues)

	redirect = RedirectResponse(url = "/verify") 
	return redirect

@app.get("/thony")
async def read_item(group: str, q:Union[str, None] = None):
	text = getGpt(group)
	return {"message": f'{text}'}

@app.get("/allusers")
async def root():
	myclient = pymongo.MongoClient("mongodb://127.0.0.1:27017")
	db = myclient["push"]
	col = db["users"]
	ret = col.find({}, {'_id': 0})
	print(ret)
	return loads(dumps(ret))

@app.get("/alltasks")
async def root():
	myclient = pymongo.MongoClient("mongodb://127.0.0.1:27017")
	db = myclient["push"]
	col = db["tasks"]
	ret = col.find({}, {'_id': 0})
	print(ret)
	return loads(dumps(ret))
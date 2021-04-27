const express = require('express');
const bodyParser = require('body-parser');
const uploadRouter = express.Router();
const authenticate = require('../authenticate')
const multer = require('multer')
const  cors  = require('./cors');
const passport = require('passport');

uploadRouter.use(bodyParser.json())

var storage = multer.diskStorage(
    {
        destination: (req,file,cb) =>{
            cb(null,'public/images')
        },
        filename: (req, file,cb) => {
            cb(null,file.originalname)
        }
    }
)

var imageFileFilter = (req,file,cb) =>{
    if(!file.originalname.match(/\.(jpg|jpeg|png|gif)$/)){
        return cb(new Error('You can upload images only',false))
    }
    else{
        cb(null, true)
    }
}

const uplaod = multer({
    storage:storage,
    fileFilter:imageFileFilter
})

uploadRouter.route('/')
.options(cors.corsWithOptions,(req,res) => {res.sendStatus(200)})
.post(cors.corsWithOptions,authenticate.verifyUser,uplaod.single('imageFile'),(req,res) =>{
    res.statusCode = 200
    res.setHeader('Content-Type','application/json')
    var path = req.file.destination.replace('/public','')
    res.json(`http://192.168.1.12:3000/${path}/${req.file.filename}`)
})


module.exports = uploadRouter
var express = require("express");
var router = express.Router();
const bodyParser = require("body-parser");
const Users = require("../models/user");
const passport = require('passport');
const  cors  = require('./cors')
const  authenticate  = require("../authenticate");

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

router.post('/signUp',cors.corsWithOptions,(req,res,next) =>{
   Users.register({username:req.body.username},req.body.password,(err,user) =>{
       if(err){
        res.statusCode = 500
        res.setHeader('Content-Type','application/json')
        res.json({err:err})
       }
       else{
        if(req.body.firstName){
           user.firstName = req.body.firstName
        }
        if(req.body.lastName){
          user.lastName = req.body.lastName
        }
        user.save()
        .then((user) =>{
 
           passport.authenticate('local')(req,res,() =>{
               res.statusCode = 200
               res.setHeader('Content-Type','application/json')
               res.json({success:true,status:"Registration success"})
           })
 
        },(err) => next(err))
        .catch((err) =>{
           res.statusCode = 500
           res.setHeader('Content-Type','application/json')
           res.json({err:err})
        })
         
      }

   })
})

router.post("/login",cors.corsWithOptions, passport.authenticate('local'), (req, res) => {

  var token = authenticate.getToken({_id:req.user._id})

  res.statusCode = 200;
  res.setHeader('Content-Type', 'application/json');
  res.json({success: true, token:token , status: 'You are successfully logged in!'});
  
});

module.exports = router;

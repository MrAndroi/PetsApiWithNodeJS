const passport = require('passport')
const passportMongoose = require('passport-local-mongoose')
const LocalStrategy = require('passport-local').Strategy
const JwtStrategy = require('passport-jwt').Strategy
const ExtractJwt = require('passport-jwt').ExtractJwt
const jwt = require('jsonwebtoken')
const User = require('./models/user')
const config = require('./config')


exports.local = passport.use(new LocalStrategy((User.authenticate())))
passport.serializeUser(User.serializeUser())
passport.deserializeUser(User.deserializeUser())


exports.getToken = function(user){
    return jwt.sign(user,config.secretKey,{expiresIn:100000})
}

const opt = {};
opt.jwtFromRequest = ExtractJwt.fromAuthHeaderAsBearerToken()
opt.secretOrKey = config.secretKey

exports.jwtStrategy = passport.use(new JwtStrategy(opt,(jwt_payload,done) =>{
    User.findOne({_id:jwt_payload._id})
    .then((user) =>{
        done(null,user)
    },(err) => done(err,false))
    .catch((err) =>{
        done(err,false)
    })
}))

exports.verifyUser = passport.authenticate('jwt',{session:false})

exports.verifyAdmin = function(req,res,next){
    if (req.user.admin) {
        next();
    } else {
        var err = new Error('You are not authorized to perform this operation!');
        err.status = 403;
        return next(err);
    }
}



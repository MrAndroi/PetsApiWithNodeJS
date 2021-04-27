const mongoose = require('mongoose')
const Schema = mongoose.Schema

const commentSchema = new Schema({
    author:{
        type:mongoose.SchemaTypes.ObjectId,
        ref:'User'
    },
    comment:{
        type:String,
        required:true
    }
}, {timestamps:true})

const petSchema = new Schema({
    petName:{
        type:String,
        required:true
    },
    petAge:{
        type:Number,
        required:true,
        min:1,
        max:50
    
    },
    petBreed:{
        type:String,
        required:true
    },
    petColor:{
        type:String,
        required:true,
    },
    ownerId:{
        type:mongoose.SchemaTypes.ObjectId,
        ref:'User'
    },
    petImage:{
        type:String,
        required:false
    },
    comments:[commentSchema]
}, {timestamps:true})

var Pet = mongoose.model('pet',petSchema)

module.exports = Pet
const express = require('express')
const petsRouter = express.Router()
const bodyParser = require('body-parser');
const authenticate = require('../authenticate')
const Pets = require('../models/pet')

petsRouter.use(bodyParser.json())


petsRouter.route('/')
.get((req,res,next) =>{

    Pets.find({})
    .populate('ownerId')
    .populate('comments.author')
    .then((pets) =>{

        res.statusCode = 200
        res.setHeader('Content-Type','application/json')
        res.json(pets)

    },(err) => next(err))
    .catch((err) =>{
        next(err)
    })
})
.put(authenticate.verifyUser,(req,res,next) =>{
    res.statusCode = 406
    res.end('PUT operation is not allowed on /pets')
})
.post(authenticate.verifyUser,(req,res,next) =>{
    Pets.create(req.body)
    .then((pets) =>{
       if(pets instanceof Array){
            for(var i = pets.length-1 ; i >=0 ; i--){
                pets[i].ownerId = req.user._id
                pets[i].save()
            }
            res.statusCode = 200
            res.setHeader('Contect-Type','application/json')
            res.json({status:true,pets:pets,message:'Pets added successfully'})
       }
       else{
        pets.ownerId = req.user._id
        pets.save()
        .then((pets) =>{
            
            res.statusCode = 200
            res.setHeader('Contect-Type','application/json')
            res.json({status:true,pets:pets,message:'Pets added successfully'})
        })
       }
      
    },(err) => next(err))
    .catch((err) =>{
        next(err)
    })
})
.delete(authenticate.verifyUser,(req,res,next) =>{
    res.statusCode = 406
    res.end('DELETE operation is not allowed on /pets')
})
.patch

petsRouter.route('/:petId')
.get(authenticate.verifyUser,(req,res,next) =>{

    Pets.findOne({_id:req.params.petId})
    .then((pet) =>{
        if(pet._id == req.user._id){
            res.statusCode = 200
            res.setHeader('Content-Type','application/json')
            res.json({pet:pet,onwer:true})
        }
        else{
            res.statusCode = 200
            res.setHeader('Content-Type','application/json')
            res.json({pet:pet,owner:false})
        }

    },(err) => next(err))
    .catch((err) =>{
        next(err)
    })
})
.put(authenticate.verifyUser,(req,res,next) =>{

    Pets.findOneAndUpdate({_id:req.params.petId},req.body,{new:true})
    .then((newPet) =>{

        res.statusCode = 200
        res.setHeader('Content-Type','application/json')
        res.json({status:true,newPet:newPet,message:'Pet updated successfully'})

    },(err) => next(err))
    .catch((err) =>{
        next(err)
    })

})
.post(authenticate.verifyUser,(req,res,next) =>{
    res.statusCode = 406
    res.end(`POST operation is not allowed on /pets/${req.params.petId}`)
})
.delete(authenticate.verifyUser,(req,res,next) =>{
    Pets.findOne({_id:req.params.petId})
    .then((pet) =>{

        if(pet.ownerId.equals(req.user._id)){
            Pets.findOneAndDelete({_id:req.params.petId})
            .then((pet) =>{
                res.statusCode = 200
                res.setHeader('Content-Type','application/json')
                res.json({status:true,petDeleted:pet,message:`Pet ${pet.petName} successfully deleted`})
            })
        }
        else{

            res.statusCode = 406
            res.end('You are not the owner of this pet')
        }

    },(err) => next(err))
    .catch((err) =>{
        next(err)
    })
})

module.exports = petsRouter

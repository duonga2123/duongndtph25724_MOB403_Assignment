const mongoose = require('mongoose');
const ProductSchema = new mongoose.Schema({
    productname: {
        type: String,
        require: true
    },
    price: {
        type: Number,
        require: true
    },
    producttype: {
        type: String,
        require: true
    },
    productimg:{
        type: String,
        require: true
    },

})

const ProductModel = new mongoose.model('products',ProductSchema);
module.exports = ProductModel;
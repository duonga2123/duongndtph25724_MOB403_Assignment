const express = require('express');
const mongoose = require('mongoose');

const uri = "mongodb+srv://thanhduonga2123:tQpKxiFkhoFoia51@cluster0.b9fispt.mongodb.net/sanpham";
const bodyParser = require('body-parser');
const ProductModel = require('./model/ProductModel');

mongoose.connect(uri,{
    useNewUrlParser: true,
    useUnifiedTopology: true,
}) 
.then(() => {
    console.log('Da ket noi voi MongoDB');
})
.catch((err) => {
    console.error('Khong ket noi dc MongoDB: ', err);
})

const app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.get('/listProducts', async function (req, res) {

    try {
        const productList = await ProductModel.find().lean();
        console.log('Da ket noi voi MongoDB');
        res.json(productList);
      } catch (err) {
        console.error('Error fetching products:', err);
        res.status(500).json({ error: 'Internal server error' });
      }
})

// Định nghĩa API thêm sản phẩm
app.post('/addProducts', async (req, res) => {
  try {
    const { productname, price, producttype, productimg } = req.body;
    const product = new ProductModel({  productname, price, producttype, productimg });
    await product.save();
    //res.json(product);
    const productList = await ProductModel.find().lean();
    res.json(productList);
  } catch (err) {
    console.error('Error adding product:', err);
    res.status(500).json({ error: 'Internal server error' });
  }
});

// Định nghĩa API sửa đổi sản phẩm
app.put('/Products/:productId', async (req, res) => {
  try {
    const { productId } = req.params;
    const { productname, price, producttype, productimg } = req.body;
    const product = await ProductModel.findByIdAndUpdate(
      productId,
      {  productname, price, producttype, productimg },
      { new: true }
    );
    const productList = await ProductModel.find().lean();
    res.json(productList);
    //res.json(product);
  } catch (err) {
    console.error('Error updating product:', err);
    res.status(500).json({ error: 'Internal server error' });
  }
});

// Định nghĩa API xóa sản phẩm
app.delete('/Products/:productId', async (req, res) => {
  try {
    const { productId } = req.params;
    await ProductModel.findByIdAndRemove(productId);
    const productList = await ProductModel.find().lean();
    res.json(productList);
    //res.json({ success: true });
  } catch (err) {
    console.error('Error deleting product:', err);
    res.status(500).json({ error: 'Internal server error' });
  }
});

  
  // Khởi động server
  const port = 8000;
  app.listen(port, () => {
    console.log(`Server started on port ${port}`);
  });
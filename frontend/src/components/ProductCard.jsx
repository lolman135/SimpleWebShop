import React from "react";
import "./ProductCard.css"; // отдельный css для карточек (ниже)

const ProductCard = ({ product }) => {

    const priceUnit = (product.price / 100).toFixed(2)

    return (
        <div className="product-card">
            <img src={product.imageUrl} alt={product.name} className="product-image" />
            <h3>{product.name}</h3>
            <p className="price">$ {priceUnit}</p>
            <p className="description">{product.description}</p>
        </div>
    );
};

export default ProductCard;

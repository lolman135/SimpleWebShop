import React, { useEffect, useState } from "react";
import axios from "axios";
import ProductCard from "../components/ProductCard";
import "./Home.css"; // стили для сетки

const Home = () => {
    const [products, setProducts] = useState([]);

    useEffect(() => {
        axios
            .get("http://localhost:8080/api/v1/products")
            .then((res) => setProducts(res.data))
            .catch((err) => console.error(err));
    }, []);

    const groupedByCategory = products.reduce((acc, product) => {
        const cat = product.category || "Без категории";
        if (!acc[cat]) acc[cat] = [];
        acc[cat].push(product);
        return acc;
    }, {});

    return (
        <div>
            <h2>Our Products</h2>
            {Object.keys(groupedByCategory).map((category) => (
                <div key={category} className="category-block">
                    <h3 className="category-title">{category}</h3>
                    <div className="product-grid">
                        {groupedByCategory[category].map((p) => (
                            <ProductCard key={p.id} product={p} />
                        ))}
                    </div>
                </div>
            ))}
        </div>
    );
};

export default Home;

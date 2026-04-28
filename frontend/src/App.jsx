import React, { useState, useEffect } from 'react';
import axios from 'axios';

// const API_URL = "http://localhost:8080/api/books";
const API_BASE_URL = "https://library-management-system-g1rg.onrender.com/api/books";

function App() {
  const [books, setBooks] = useState([]);
  const [formData, setFormData] = useState({ title: '', author: '', isbn: '' });
  const [editingId, setEditingId] = useState(null);

  const fetchBooks = async () => {
    const response = await axios.get(API_URL);
    setBooks(response.data);
  };

  useEffect(() => { fetchBooks(); }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (editingId) {
      await axios.put(`${API_URL}/${editingId}`, formData);
      setEditingId(null);
    } else {
      await axios.post(API_URL, formData);
    }
    setFormData({ title: '', author: '', isbn: '' });
    fetchBooks();
  };

  const handleEdit = (book) => {
    setEditingId(book.id);
    setFormData({ title: book.title, author: book.author, isbn: book.isbn });
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure?")) {
      await axios.delete(`${API_URL}/${id}`);
      fetchBooks();
    }
  };

  return (
    <div style={{ maxWidth: '800px', margin: 'auto', padding: '20px' }}>
      <h2>📚 Library Manager</h2>
      
      <form onSubmit={handleSubmit} style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
        <input placeholder="Title" value={formData.title} onChange={(e) => setFormData({...formData, title: e.target.value})} required />
        <input placeholder="Author" value={formData.author} onChange={(e) => setFormData({...formData, author: e.target.value})} required />
        <input placeholder="ISBN" value={formData.isbn} onChange={(e) => setFormData({...formData, isbn: e.target.value})} required />
        <button type="submit" style={{ backgroundColor: editingId ? '#4caf50' : '#008cba', color: 'white' }}>
          {editingId ? 'Update' : 'Add'}
        </button>
        {editingId && <button onClick={() => {setEditingId(null); setFormData({title:'', author:'', isbn:''})}}>Cancel</button>}
      </form>

      <table border="1" style={{ width: '100%', textAlign: 'left', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ backgroundColor: '#f2f2f2' }}>
            <th>Title</th><th>Author</th><th>ISBN</th><th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {books.map(book => (
            <tr key={book.id}>
              <td>{book.title}</td><td>{book.author}</td><td>{book.isbn}</td>
              <td>
                <button onClick={() => handleEdit(book)} style={{ marginRight: '5px' }}>Edit</button>
                <button onClick={() => handleDelete(book.id)} style={{ color: 'red' }}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default App;
